package com.woowa.woowakit.domain.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowa.woowakit.domain.product.exception.ProductQuantityNegativeException;

@DisplayName("Quantity 도메인 단위 테스트")
class QuantityTest {

	@Test
	@DisplayName("수량은 0보다 작을 수 없다.")
	void negativeProductQuantityError() {
		assertThatCode(() -> Quantity.from(-1L))
			.isInstanceOf(ProductQuantityNegativeException.class)
			.hasMessage("제품 수량은 0보다 작을 수 없습니다.");
	}

	@Test
	@DisplayName("수량의 합을 구할 수 있다.")
	void add() {
		// given
		Quantity quantity1 = Quantity.from(1);
		Quantity quantity2 = Quantity.from(2);

		// when
		Quantity result = quantity1.add(quantity2);

		// then
		assertThat(result).extracting("quantity").isEqualTo(3L);

	}
}
