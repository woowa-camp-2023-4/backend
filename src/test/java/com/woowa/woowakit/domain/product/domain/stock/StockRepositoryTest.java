package com.woowa.woowakit.domain.product.domain.stock;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

@DataJpaTest
class StockRepositoryTest {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void findByProductIdAndExpiryDate() {
		// given
		LocalDate expiryDate = LocalDate.now().plusDays(1);
		Product product = productRepository.save(Product.of("product", 1000L, "test.jpg"));
		Stock stock = stockRepository.save(Stock.of(expiryDate, product));

		// when
		Stock result = stockRepository.findByProductIdAndExpiryDate(product.getId(), ExpiryDate.from(expiryDate)).get();

		// then
		assertThat(result).isSameAs(stock);
	}
}