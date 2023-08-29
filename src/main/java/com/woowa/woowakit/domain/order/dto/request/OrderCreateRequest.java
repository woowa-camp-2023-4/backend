package com.woowa.woowakit.domain.order.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderCreateRequest {

	@NotNull(message = "결제 키는 필수값입니다.")
	private String paymentKey;

	public static OrderCreateRequest of(final String paymentKey) {
		return new OrderCreateRequest(paymentKey);
	}
}
