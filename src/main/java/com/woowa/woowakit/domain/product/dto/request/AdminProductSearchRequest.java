package com.woowa.woowakit.domain.product.dto.request;

import javax.validation.constraints.Min;

import com.woowa.woowakit.domain.product.domain.product.AdminProductSearchCondition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class AdminProductSearchRequest {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private String productKeyword;

	private Long lastProductId;

	@Min(value = 1, message = "최소 1개 이상의 상품을 조회해야합니다.")
	private int pageSize = DEFAULT_PAGE_SIZE;

	public static AdminProductSearchRequest of(
		final String productKeyword,
		final Long lastProductId,
		final int pageSize
	) {
		return new AdminProductSearchRequest(productKeyword, lastProductId, pageSize);
	}

	public AdminProductSearchCondition toAdminProductSearchCondition() {
		return AdminProductSearchCondition.builder()
			.productKeyword(productKeyword)
			.lastProductId(lastProductId)
			.pageSize(pageSize)
			.build();
	}
}
