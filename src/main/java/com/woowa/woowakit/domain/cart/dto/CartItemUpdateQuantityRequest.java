package com.woowa.woowakit.domain.cart.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class CartItemUpdateQuantityRequest {

	@NotNull(message = "수량 입력은 필수입니다.")
	private Long quantity;
}
