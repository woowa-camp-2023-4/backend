package com.woowa.woowakit.domain.cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CartItemAddRequest {

	@NotNull(message = "상품 ID 는 필수입니다.")
	private Long productId;

	@Min(value = 1, message = "상품 수량은 1개이상이어야 합니다.")
	private long quantity;

	public static CartItemAddRequest of(final Long productId, final Long quantity) {
		return new CartItemAddRequest(productId, quantity);
	}
}
