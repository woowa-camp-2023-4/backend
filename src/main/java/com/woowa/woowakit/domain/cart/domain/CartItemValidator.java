package com.woowa.woowakit.domain.cart.domain;

import com.woowa.woowakit.domain.cart.exception.CartItemQuantityException;
import com.woowa.woowakit.domain.cart.exception.InvalidProductInCartItemException;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.exception.ProductNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemValidator {

    private final ProductRepository productRepository;

    void validate(final CartItem cartItem) {
        validate(cartItem, getProduct(cartItem.getProductId()));
    }

    void validate(final CartItem cartItem, final Product product) {
        if (!product.isAvailablePurchase()) {
            throw new InvalidProductInCartItemException();
        }

        if (!product.isEnoughQuantity(cartItem.getQuantity())) {
            throw new CartItemQuantityException();
        }
    }

    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotExistException::new);
    }
}
