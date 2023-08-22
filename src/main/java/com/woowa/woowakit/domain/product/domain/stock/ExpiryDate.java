package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;
import java.time.ZoneId;

import com.woowa.woowakit.domain.product.exception.StockExpiredException;

import lombok.Getter;

@Getter
public class ExpiryDate implements Comparable<ExpiryDate> {

	private final LocalDate date;

	private ExpiryDate(final LocalDate date) {
		validExpiryDate(date);
		this.date = date;
	}

	public static ExpiryDate from(final LocalDate date) {
		return new ExpiryDate(date);
	}

	private void validExpiryDate(final LocalDate date) {
		if (date.isBefore(LocalDate.now(ZoneId.of("Asia/Seoul")))) {
			throw new StockExpiredException();
		}
	}

	@Override
	public int compareTo(final ExpiryDate o) {
		return this.date.compareTo(o.date);
	}
}
