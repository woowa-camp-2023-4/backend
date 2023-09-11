package com.woowa.woowakit.shop.product.domain.product;

import com.woowa.woowakit.shop.model.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductPrice {

	private final Money price;

	public static ProductPrice from(final Money price) {
		return new ProductPrice(price);
	}

	public static ProductPrice from(final Long price) {
		return new ProductPrice(Money.from(price));
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductPrice)) return false;
		final ProductPrice that = (ProductPrice) o;
		return Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(price);
	}
}
