package com.woowa.woowakit.domain.product.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.woowa.woowakit.domain.product.domain.product.InStockProductSearchCondition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class InStockProductSearchRequest {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private static final LocalDate DEFAULT_SALE_DATE = LocalDate.now().minusDays(1);

	private String productKeyword;

	private Long lastProductId;

	private Long lastProductSale;

	@Min(value = 1, message = "최소 1개 이상의 상품을 조회해야합니다.")
	private int pageSize = DEFAULT_PAGE_SIZE;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleDate = DEFAULT_SALE_DATE;

	public static InStockProductSearchRequest of(
		final String productKeyword,
		final Long lastProductId,
		final Long lastProductSale,
		final int pageSize,
		final LocalDate saleDate
	) {
		return new InStockProductSearchRequest(productKeyword, lastProductId, lastProductSale, pageSize, saleDate);
	}

	public InStockProductSearchCondition toInStockProductSearchCondition() {
		return InStockProductSearchCondition.of(productKeyword, lastProductId, lastProductSale, pageSize, saleDate);
	}
}
