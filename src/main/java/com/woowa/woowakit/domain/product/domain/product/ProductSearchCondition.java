package com.woowa.woowakit.domain.product.domain.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductSearchCondition {

	private String productKeyword;

	private Long lastProductId;

	private int pageSize;

	public static ProductSearchCondition of(final String productKeyword, final Long lastProductId, final int pageSize) {
		return new ProductSearchCondition(productKeyword, lastProductId, pageSize);
	}
}
