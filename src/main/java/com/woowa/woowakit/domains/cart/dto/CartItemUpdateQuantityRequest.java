package com.woowa.woowakit.domains.cart.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CartItemUpdateQuantityRequest {

    @NotNull(message = "수량 입력은 필수입니다.")
    private Long quantity;

    public static CartItemUpdateQuantityRequest from(final long quantity) {
        return new CartItemUpdateQuantityRequest(quantity);
    }
}
