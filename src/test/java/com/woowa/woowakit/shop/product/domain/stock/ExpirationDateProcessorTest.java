package com.woowa.woowakit.shop.product.domain.stock;

import com.woowa.woowakit.shop.model.Quantity;
import com.woowa.woowakit.shop.product.domain.product.Product;
import com.woowa.woowakit.shop.product.domain.product.ProductName;
import com.woowa.woowakit.shop.product.domain.product.ProductPrice;
import com.woowa.woowakit.shop.product.domain.product.ProductRepository;
import com.woowa.woowakit.shop.product.fixture.ProductFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExpirationDateProcessorTest {

	@Autowired
	private ExpirationDateProcessor expirationDateProcessor;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StockRepository stockRepository;

	@AfterEach
	void setup() {
		productRepository.deleteAll();
		stockRepository.deleteAll();
	}


	@Test
	@DisplayName("재고 중 정책에 따라 유통기한이 만료된 재고는 EXPIRED 처리한다.")
	void doProcess() {
		// given
		Product product = productRepository.save(ProductFixture.anProduct()
			.price(ProductPrice.from(10000L))
			.quantity(Quantity.from(100))
			.name(ProductName.from("된장 밀키트"))
			.build());

		stockRepository.save((Stock.of(LocalDate.of(3023, 9, 11), product)));
		stockRepository.save((Stock.of(LocalDate.of(3023, 9, 12), product)));
		stockRepository.save((Stock.of(LocalDate.of(3023, 9, 13), product)));
		stockRepository.save((Stock.of(LocalDate.of(3023, 9, 14), product)));

		// when
		expirationDateProcessor.run(product, LocalDate.of(3023, 9, 5));

		// then
		assertThat(stockRepository.findAllByProductId(product.getId(), StockType.EXPIRED))
			.hasSize(1);
	}
}
