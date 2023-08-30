package com.woowa.woowakit.domain.product.domain.product;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InStockProductSearchCondition {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private static final LocalDate DEFAULT_SALE_DATE = LocalDate.now().minusDays(1);

	private String productKeyword;

	private Long lastProductId;

	private Long lastProductSale;

	@Builder.Default
	private int pageSize = DEFAULT_PAGE_SIZE;

	@Builder.Default
	private LocalDate saleDate = DEFAULT_SALE_DATE;


	public static InStockProductSearchCondition of(
		final String productKeyword,
		final Long lastProductId,
		final Long lastProductSale,
		final int pageSize,
		final LocalDate saleDate
	) {
		return new InStockProductSearchCondition(productKeyword, lastProductId, lastProductSale, pageSize, saleDate);
	}
}
