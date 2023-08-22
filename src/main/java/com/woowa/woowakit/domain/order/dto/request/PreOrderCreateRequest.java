package com.woowa.woowakit.domain.order.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PreOrderCreateRequest {

	@NotNull(message = "상품 아이디는 필수값입니다.")
	private Long productId;

	@NotNull(message = "수량은 필수값입니다.")
	private Long quantity;

	public static PreOrderCreateRequest of(final Long productId, final Long quantity) {
		return new PreOrderCreateRequest(productId, quantity);
	}
}
