package com.woowa.woowakit.domain.product.domain.stock;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowa.woowakit.domain.product.exception.StockQuantityNegativeException;

@DisplayName("StockQuantity 단위 테스트")
class StockQuantityTest {

	@Test
	@DisplayName("재고 수량은 0보다 작을 수 없다.")
	void negativeProductQuantityError() {
		assertThatCode(() -> StockQuantity.from(-1L))
			.isInstanceOf(StockQuantityNegativeException.class)
			.hasMessage("재고 수량은 0보다 작을 수 없습니다.");
	}

}