package com.woowa.woowakit.domain.product.domain.stock;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowa.woowakit.domain.product.exception.StockExpiredException;

@DisplayName("ExpiryDate 단위 테스트")
class ExpiryDateTest {

	@Test
	@DisplayName("소비 기한이 오늘 이후인 재고를 생성한다.")
	void create() {
		assertThatCode(() -> ExpiryDate.from(LocalDate.now()))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("당일 이전의 재고를 생성하면 예외가 발생한다.")
	void createExceptionIfPast() {
		assertThatCode(() -> ExpiryDate.from(LocalDate.now().minusDays(1)))
			.isInstanceOf(StockExpiredException.class)
			.hasMessage("소비 기한이 지난 재고 항목입니다.");
	}
}
