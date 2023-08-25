package com.woowa.woowakit.domain.product.domain.product;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductSearchCondition {

	private String productKeyword;

	private Long lastProductId;

	private Long lastProductSale;

	private int pageSize;

	private LocalDate saleDate;

	public static ProductSearchCondition of(
		final String productKeyword,
		final Long lastProductId,
		final Long lastProductSale,
		final int pageSize,
		final LocalDate saleDate
	) {
		return new ProductSearchCondition(productKeyword, lastProductId, lastProductSale, pageSize, saleDate);
	}
}
