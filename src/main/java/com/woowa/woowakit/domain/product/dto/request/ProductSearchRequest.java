package com.woowa.woowakit.domain.product.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.woowa.woowakit.domain.product.domain.product.ProductSearchCondition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductSearchRequest {

	private static final int DEFAULT_PAGE_SIZE = 10;

	private String productKeyword;

	private Long lastProductId;

	@Min(value = 1, message = "최소 1개 이상의 상품을 조회해야합니다.")
	private int pageSize = DEFAULT_PAGE_SIZE;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleDate = LocalDate.now().minusDays(1);

	public static ProductSearchRequest of(
		final String productKeyword,
		final Long lastProductId,
		final int pageSize,
		final LocalDate saleDate
	) {
		return new ProductSearchRequest(productKeyword, lastProductId, pageSize, saleDate);
	}

	public ProductSearchCondition toProductSearchCondition() {
		return ProductSearchCondition.of(productKeyword, lastProductId, pageSize, saleDate);
	}
}


