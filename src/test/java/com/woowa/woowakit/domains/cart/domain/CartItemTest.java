package com.woowa.woowakit.domains.cart.domain;

import com.woowa.woowakit.domains.model.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CartItemTest {

    @Test
    @DisplayName("cartItem 에 수량을 더할 수 있다.")
    void addQuantity() {
        // given
        CartItem cartItem = CartItem.of(1L, 2L, 10);

        // when
        cartItem.addQuantity(Quantity.from(9), mock(CartItemValidator.class));

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(Quantity.from(19));
    }

    @Test
    @DisplayName("cartItem 를 처음 생성하면 수량이 0이다.")
    void createCartItem() {
        // given
        CartItem cartItem = CartItem.of(1L, 2L);

        // when , then
        assertThat(cartItem.getQuantity()).isEqualTo(Quantity.from(0));
    }
}
