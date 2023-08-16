package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;

import com.woowa.woowakit.domain.product.exception.StockExpiredException;

import lombok.Getter;

@Getter
public class ExpiryDate {

	private final LocalDate date;

	private ExpiryDate(final LocalDate date) {
		validExpiryDate(date);
		this.date = date;
	}

	public static ExpiryDate from(final LocalDate date) {
		return new ExpiryDate(date);
	}

	private void validExpiryDate(final LocalDate date) {
		if (date.isBefore(LocalDate.now())) {
			throw new StockExpiredException();
		}
	}
}
