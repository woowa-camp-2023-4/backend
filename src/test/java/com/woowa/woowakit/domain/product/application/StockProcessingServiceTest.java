package com.woowa.woowakit.domain.product.application;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.stock.ExpiryDate;
import com.woowa.woowakit.domain.product.domain.stock.Stock;
import com.woowa.woowakit.domain.product.domain.stock.StockRepository;
import com.woowa.woowakit.domain.product.domain.stock.StockType;
import com.woowa.woowakit.domain.product.fixture.ProductFixture;

@SpringBootTest
class StockProcessingServiceTest {

	@Autowired
	private StockProcessingService stockProcessingService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StockRepository stockRepository;

	@Test
	@DisplayName("재고의 정합성 , 유통기한 테스트")
	void doProcessTest() {
		// given
		Product product = productRepository.save(ProductFixture.anProduct()
			.quantity(Quantity.from(75))
			.build());

		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 22), 10));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 22), 10));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 22), 20));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 28), 30));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 28), 30));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 28), 30));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 28), 30));

		// when
		stockProcessingService.doProcess(product.getId(), LocalDate.of(2023, 9, 16));

		// then
		Assertions.assertThat(stockRepository.findAllByProductId(product.getId(), StockType.NORMAL))
			.hasSize(3)
			.extracting("quantity")
			.contains(Quantity.from(15), Quantity.from(30), Quantity.from(30));
	}

	private Stock createStock(Product product, LocalDate date, long quantity) {
		return Stock.builder()
			.expiryDate(ExpiryDate.from(date))
			.product(product)
			.quantity(Quantity.from(quantity))
			.build();
	}
}
