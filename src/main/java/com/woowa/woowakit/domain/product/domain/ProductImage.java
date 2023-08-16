package com.woowa.woowakit.domain.product.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductImage {

	private final String path;

	public static ProductImage from(final String path) {
		return new ProductImage(path);
	}
}
