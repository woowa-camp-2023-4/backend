package com.woowa.woowakit.domain.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductName {

	private String name;

	public static ProductName of(final String name) {
		return new ProductName(name);
	}
}
