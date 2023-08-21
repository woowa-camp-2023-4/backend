package com.woowa.woowakit.domain.product.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Null;

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

	@Null
	private String productKeyword;

	@Null
	private Long lastProductId;

	@Min(value = 1, message = "최소 1개 이상의 상품을 조회해야합니다.")
	private int pageSize;

	public static ProductSearchRequest of(final String productKeyword, final Long lastProductId, final int pageSize) {
		return new ProductSearchRequest(productKeyword, lastProductId, pageSize);
	}

	public ProductSearchCondition toProductSearchCondition() {
		return ProductSearchCondition.of(productKeyword, lastProductId, pageSize);
	}
}


