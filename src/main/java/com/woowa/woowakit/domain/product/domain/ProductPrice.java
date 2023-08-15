package com.woowa.woowakit.domain.product.domain;

import com.woowa.woowakit.domain.model.Money;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductPrice {

	private final Money price;

	public static ProductPrice from(final Money price) {
		return new ProductPrice(price);
	}

	public static ProductPrice from(final Long price) {
		return new ProductPrice(Money.from(price));
	}
}
