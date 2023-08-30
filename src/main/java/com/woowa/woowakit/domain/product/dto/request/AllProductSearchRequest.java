package com.woowa.woowakit.domain.product.dto.request;

import javax.validation.constraints.Min;

import com.woowa.woowakit.domain.product.domain.product.AllProductSearchCondition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class AllProductSearchRequest {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private String productKeyword;

	private Long lastProductId;

	@Min(value = 1, message = "최소 1개 이상의 상품을 조회해야합니다.")
	private int pageSize = DEFAULT_PAGE_SIZE;

	public static AllProductSearchRequest of(
		final String productKeyword,
		final Long lastProductId,
		final int pageSize
	) {
		return new AllProductSearchRequest(productKeyword, lastProductId, pageSize);
	}

	public AllProductSearchCondition toAllProductSearchCondition() {
		return AllProductSearchCondition.builder()
			.productKeyword(productKeyword)
			.lastProductId(lastProductId)
			.pageSize(pageSize)
			.build();
	}
}
