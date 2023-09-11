package com.woowa.woowakit.shop.cart.domain;

import com.woowa.woowakit.shop.cart.domain.CartItem;
import com.woowa.woowakit.shop.cart.domain.CartItemValidator;
import com.woowa.woowakit.shop.cart.exception.CartItemQuantityException;
import com.woowa.woowakit.shop.cart.exception.InvalidProductInCartItemException;
import com.woowa.woowakit.shop.model.Quantity;
import com.woowa.woowakit.shop.product.domain.product.Product;
import com.woowa.woowakit.shop.product.domain.product.ProductRepository;
import com.woowa.woowakit.shop.product.domain.product.ProductStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.mockito.Mockito.mock;

class CartItemValidatorTest {

    private CartItemValidator cartItemValidator;

    @BeforeEach
    void setUp() {
        cartItemValidator = new CartItemValidator(mock(ProductRepository.class));
    }

    @Test
    @DisplayName("상품 재고보다 장바구니에 딤은 수량이 적은 경우 예외를 던지지 않는다.")
    void validateNotThrow() {
        // given
        Product product = Product.builder()
                .quantity(Quantity.from(20))
                .status(ProductStatus.IN_STOCK)
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
        Product product = Product.builder()
                .quantity(Quantity.from(2))
                .status(ProductStatus.IN_STOCK)
                .build();
        CartItem cartItem = CartItem.builder()
                .quantity(Quantity.from(10))
                .build();

        // when , then
        Assertions.assertThatCode(() -> cartItemValidator.validate(cartItem, product))
                .isInstanceOf(CartItemQuantityException.class)
                .hasMessage("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.");
    }

    @ParameterizedTest
    @EnumSource(value = ProductStatus.class, names = {"IN_STOCK"}, mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("상품을 구매할 수 있는 상태가 아니면 장바구니에 담을 때 예외가 발생한다")
    void isNotAvailableForPurchase(final ProductStatus productStatus) {
        // given
        Product product = Product.builder()
                .quantity(Quantity.from(20))
                .status(productStatus)
                .build();
        CartItem cartItem = CartItem.builder()
                .quantity(Quantity.from(10))
                .build();

        // when , then
        Assertions.assertThatCode(() -> cartItemValidator.validate(cartItem, product))
                .isInstanceOf(InvalidProductInCartItemException.class)
                .hasMessage("상품을 구매할 수 없는 상태입니다.");
    }
}
