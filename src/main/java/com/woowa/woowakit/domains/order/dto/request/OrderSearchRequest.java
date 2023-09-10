package com.woowa.woowakit.domains.order.dto.request;

import javax.validation.constraints.Min;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class OrderSearchRequest {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private Long lastOrderId;

	@Min(value = 1, message = "최소 1개 이상의 주문을 조회해야합니다.")
	private int pageSize = DEFAULT_PAGE_SIZE;

	public static OrderSearchRequest of(
		final Long lastOrderId,
		final int pageSize
	) {
		return new OrderSearchRequest(lastOrderId, pageSize);
	}
}
