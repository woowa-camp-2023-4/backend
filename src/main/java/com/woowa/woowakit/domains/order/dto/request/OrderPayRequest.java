package com.woowa.woowakit.domains.order.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderPayRequest {

	@NotNull(message = "결제 키는 필수값입니다.")
	private String paymentKey;

	public static OrderPayRequest of(final String paymentKey) {
		return new OrderPayRequest(paymentKey);
	}
}
