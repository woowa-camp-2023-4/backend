package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.Money;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TotalPrice {

	private final Money money;

	public static TotalPrice from(final Long money) {
		return new TotalPrice(Money.from(money));
	}
}
