package com.woowa.woowakit.domain.cart.domain;

import com.woowa.woowakit.domain.cart.exception.CartItemQuantityException;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CartItemValidatorTest {

    @Test
    @DisplayName("상품 재고보다 장바구니에 딤은 수량이 적은 경우 예외를 던지지 않는다.")
    void validateNotThrow() {
        // given
        CartItemValidator cartItemValidator = new CartItemValidator(mock(ProductRepository.class));
        Product product = Product.builder()
                .quantity(Quantity.from(20))
                .build();
        CartItem cartItem = CartItem.builder()
                .quantity(Quantity.from(10))
                .build();

        // when , then
        Assertions.assertThatCode(() -> cartItemValidator.validate(cartItem, product))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("상품 재고보다 장바구니에 딤은 수량이 적은 경우 예외를 던진다.")
    void validateThrow() {
        // given
        CartItemValidator cartItemValidator = new CartItemValidator(mock(ProductRepository.class));
        Product product = Product.builder()
                .quantity(Quantity.from(2))
                .build();
        CartItem cartItem = CartItem.builder()
                .quantity(Quantity.from(10))
                .build();

        // when , then
        Assertions.assertThatCode(() -> cartItemValidator.validate(cartItem, product))
                .isInstanceOf(CartItemQuantityException.class)
                .hasMessage("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.");
    }
}