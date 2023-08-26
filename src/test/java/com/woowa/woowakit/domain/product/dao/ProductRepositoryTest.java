package com.woowa.woowakit.domain.product.dao;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.ProductSalesRepository;
import com.woowa.woowakit.domain.product.domain.product.*;
import com.woowa.woowakit.domain.product.fixture.ProductFixture;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

@DisplayName("ProductRepository 단위 테스트")
@DataJpaTest
@Import({QuerydslTestConfig.class, JpaConfig.class})
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSalesRepository productSalesRepository;

    @Test
    @DisplayName("상품을 검색 조건에 따라 커서 기반으로 검색한다.(첫 페이지)")
    void search() {
        // given
        Product product1 = Product.of("테스트1", 1500L, "testImg1");
        Product product2 = Product.of("테스트2", 1500L, "testImg2");
        Product product3 = Product.of("테스트3", 1500L, "testImg3");
        Product product4 = Product.of("테스트11", 1500L, "testImg4");
        Product product5 = Product.of("테스트12", 1500L, "testImg5");

        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));

        // when
        ProductSearchCondition productSearchCondition = ProductSearchCondition.of("1", null, 5, LocalDate.now());
        List<Product> result = productRepository.searchProducts(productSearchCondition);

        // then
        Assertions.assertThat(result).hasSize(3);
        Assertions.assertThat(result.get(0)).extracting(Product::getId).isEqualTo(product1.getId());
        Assertions.assertThat(result.get(1)).extracting(Product::getId).isEqualTo(product4.getId());
        Assertions.assertThat(result.get(2)).extracting(Product::getId).isEqualTo(product5.getId());
    }

    @Test
    @DisplayName("상품을 검색 조건에 따라 커서 기반으로 검색한다.(두 번째 페이지)")
    void searchCursor() {
        // given
        Product product1 = Product.of("테스트1", 1500L, "testImg1");
        Product product2 = Product.of("테스트2", 1500L, "testImg2");
        Product product3 = Product.of("테스트3", 1500L, "testImg3");
        Product product4 = Product.of("테스트11", 1500L, "testImg4");
        Product product5 = Product.of("테스트12", 1500L, "testImg5");

        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));

        // when
        ProductSearchCondition productSearchCondition = ProductSearchCondition.of("테스트", product2.getId(), 2,
                LocalDate.now());
        List<Product> result = productRepository.searchProducts(productSearchCondition);

        // then
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0)).extracting(Product::getId).isEqualTo(product3.getId());
        Assertions.assertThat(result.get(1)).extracting(Product::getId).isEqualTo(product4.getId());
    }

    @Test
    @DisplayName("상품 판매량 순 조회")
    void searchProductsTest() {
        Product productA = productRepository.save(ProductFixture.anProduct()
                .name(ProductName.from("productA"))
                .build());
        Product productB = productRepository.save(ProductFixture.anProduct()
                .name(ProductName.from("productB"))
                .build());
        Product productC = productRepository.save(ProductFixture.anProduct()
                .name(ProductName.from("productC"))
                .build());
        Product productD = productRepository.save(ProductFixture.anProduct()
                .name(ProductName.from("productD"))
                .build());

        productSalesRepository.save(createProductSale(productA, 10, LocalDate.of(2023, 8, 25)));
        productSalesRepository.save(createProductSale(productB, 20, LocalDate.of(2023, 8, 25)));
        productSalesRepository.save(createProductSale(productC, 30, LocalDate.of(2023, 8, 25)));

        ProductSearchCondition condition = ProductSearchCondition.of(null, 0L, 10, LocalDate.of(2023, 8, 25));
        List<Product> products = productRepository.searchProducts(condition);
        Assertions.assertThat(products).hasSize(4)
                .extracting("name")
                .contains(
                        ProductName.from("productC"),
                        ProductName.from("productB"),
                        ProductName.from("productA"),
                        ProductName.from("productD"));
    }

    private ProductSales createProductSale(Product productA, long quantity, LocalDate saleDate) {
        return ProductSales.builder()
                .productId(productA.getId())
                .sale(Quantity.from(quantity))
                .saleDate(saleDate)
                .build();
    }
}
