package com.woowa.woowakit.domain.product.domain.product;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductName {

	private String name;

	public static ProductName from(final String name) {
		return new ProductName(name);
	}
}
