package com.woowa.woowakit.domain.product.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductName {

	private final String name;

	public static ProductName from(final String name) {
		return new ProductName(name);
	}
}
