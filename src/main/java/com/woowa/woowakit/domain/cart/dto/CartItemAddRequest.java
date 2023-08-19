package com.woowa.woowakit.domain.cart.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CartItemAddRequest {

    @NotNull
    private Long productId;

    @Min(value = 1)
    private long quantity;

    public static CartItemAddRequest of(final Long productId, final Long quantity) {
        return new CartItemAddRequest(productId, quantity);
    }
}
