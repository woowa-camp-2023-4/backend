package com.woowa.woowakit.domain.product.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowa.woowakit.domain.product.exception.ProductQuantityNegativeException;

@DisplayName("ProductQuantity 도메인 단위 테스트")
class ProductQuantityTest {

	@Test
	@DisplayName("제품 수량은 0보다 작을 수 없다.")
	void negativeProductQuantityError() {
		assertThatCode(() -> ProductQuantity.from(-1L))
			.isInstanceOf(ProductQuantityNegativeException.class)
			.hasMessage("제품 수량은 0보다 작을 수 없습니다.");
	}
}
