package com.woowa.woowakit.domains.product.domain.stock;

import com.woowa.woowakit.domains.product.domain.product.Product;
import com.woowa.woowakit.domains.product.domain.product.ProductRepository;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QuerydslTestConfig.class, JpaConfig.class})
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByProductIdAndExpiryDate() {
        // given
        LocalDate expiryDate = LocalDate.of(2223, 12, 31);
        Product product = productRepository.save(Product.of("product", 1000L, "test.jpg"));
        Stock stock = stockRepository.save(Stock.of(expiryDate, product));

        // when
        Stock result = stockRepository.findByProductIdAndExpiryDate(product.getId(), ExpiryDate.from(expiryDate)).get();

        // then
        assertThat(result).isSameAs(stock);
    }
}
