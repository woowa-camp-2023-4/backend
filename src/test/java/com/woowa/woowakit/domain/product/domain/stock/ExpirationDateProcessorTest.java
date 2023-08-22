package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductImage;
import com.woowa.woowakit.domain.product.domain.product.ProductName;
import com.woowa.woowakit.domain.product.domain.product.ProductPrice;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.product.ProductStatus;

@SpringBootTest
class ExpirationDateProcessorTest {

	@Autowired
	private ExpirationDateProcessor expirationDateProcessor;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StockRepository stockRepository;

	@Test
	@DisplayName("재고 중 정책에 따라 유통기한이 만료된 재고는 EXPIRED 처리한다.")
	void doProcess() {

		Product product = productRepository.save(Product.builder()
			.price(ProductPrice.from(10000L))
			.quantity(Quantity.from(100))
			.imageUrl(ProductImage.from("/path"))
			.name(ProductName.from("된장 밀키트"))
			.status(ProductStatus.IN_STOCK)
			.build());

		stockRepository.save((Stock.of(LocalDate.of(2023, 9, 10), product)));
		stockRepository.save((Stock.of(LocalDate.of(2023, 9, 11), product)));
		stockRepository.save((Stock.of(LocalDate.of(2023, 9, 12), product)));
		stockRepository.save((Stock.of(LocalDate.of(2023, 9, 13), product)));
		stockRepository.save((Stock.of(LocalDate.of(2023, 9, 14), product)));

		expirationDateProcessor.doProcess(product.getId(), LocalDate.of(2023, 9, 5));

		Assertions.assertThat(stockRepository.findAllByProductId(product.getId(), StockType.EXPIRED))
			.hasSize(2);
	}
}
