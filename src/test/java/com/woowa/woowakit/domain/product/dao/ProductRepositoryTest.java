package com.woowa.woowakit.domain.product.dao;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.product.ProductSearchCondition;
import com.woowa.woowakit.global.config.QuerydslTestConfig;

@DisplayName("ProductRepository 단위 테스트")
@DataJpaTest
@Import(QuerydslTestConfig.class)
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

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
		ProductSearchCondition productSearchCondition = ProductSearchCondition.of("1", null, 5);
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
		ProductSearchCondition productSearchCondition = ProductSearchCondition.of("테스트", product2.getId(), 2);
		List<Product> result = productRepository.searchProducts(productSearchCondition);

		// then
		Assertions.assertThat(result).hasSize(2);
		Assertions.assertThat(result.get(0)).extracting(Product::getId).isEqualTo(product3.getId());
		Assertions.assertThat(result.get(1)).extracting(Product::getId).isEqualTo(product4.getId());
	}
}
