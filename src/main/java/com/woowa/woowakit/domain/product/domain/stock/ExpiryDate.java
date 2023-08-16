package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class ExpiryDate {

	private final LocalDate date;

	private ExpiryDate(final LocalDate date) {
		this.date = date;
	}

	public static ExpiryDate from(final LocalDate date) {
		return new ExpiryDate(date);
	}
}
