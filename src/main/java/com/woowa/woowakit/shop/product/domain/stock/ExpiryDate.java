package com.woowa.woowakit.shop.product.domain.stock;

import com.woowa.woowakit.shop.product.exception.StockExpiredException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ExpiryDate)) return false;
		final ExpiryDate that = (ExpiryDate) o;
		return Objects.equals(date, that.date);
	}

	@Override
	public int hashCode() {
		return Objects.hash(date);
	}
}
