package com.woowa.woowakit.domain.product.domain.stock;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

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
class StockConsistencyProcessorTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StockConsistencyProcessor stockConsistencyProcessor;

	@Test
	@DisplayName("상품 수량과 재고 테이블의 정합성을 맞춘다.")
	void doProcessTest() {
		// given
		Product product = productRepository.save(Product.builder()
			.price(ProductPrice.from(10000L))
			.quantity(Quantity.from(35))
			.imageUrl(ProductImage.from("/path"))
			.name(ProductName.from("된장 밀키트"))
			.status(ProductStatus.IN_STOCK)
			.build());

		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 22), 10));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 25), 20));
		stockRepository.save(createStock(product, LocalDate.of(2023, 9, 28), 30));

		// when
		stockConsistencyProcessor.doProcess(product);

		// then
		List<Stock> stocks = stockRepository.findAllByProductId(product.getId(), StockType.NORMAL);
		assertThat(stocks).hasSize(2)
			.extracting("quantity")
			.contains(Quantity.from(5), Quantity.from(30));
	}

	private Stock createStock(Product product, LocalDate date, long quantity) {
		return Stock.builder()
			.expiryDate(ExpiryDate.from(date))
			.product(product)
			.quantity(Quantity.from(quantity))
			.build();
	}
}
