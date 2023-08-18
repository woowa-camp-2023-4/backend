package com.woowa.woowakit.domain.cart.application;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domain.cart.exception.CartItemQuantityException;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.exception.ProductNotExistException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	@Transactional
	public CartItem addCartItem(final CartItemAddRequest request, final Long memberId) {
		Product product = getProductById(request);
		Optional<CartItem> existsCartItem = findCartItemByProductIdAndMemberId(memberId, product.getId());

		if (existsCartItem.isPresent()) {
			final CartItem cartItem = existsCartItem.get();
			cartItem.addQuantity(Quantity.from(request.getQuantity()));
			validateQuantity(cartItem.getQuantity(), product.getQuantity());
			return cartItem;
		}

		validateQuantity(Quantity.from(request.getQuantity()), product.getQuantity());
		return cartItemRepository.save(CartItem.of(memberId, request.getProductId(), request.getQuantity()));
	}

	private Optional<CartItem> findCartItemByProductIdAndMemberId(final Long memberId, final Long productId) {
		return cartItemRepository.findAllByMemberId(memberId).stream()
			.filter(cartItem -> Objects.equals(cartItem.getProductId(), productId))
			.findAny();
	}

	private void validateQuantity(final Quantity requiredQuantity, final Quantity existsQuantity) {
		if (existsQuantity.getQuantity() < requiredQuantity.getQuantity()) {
			throw new CartItemQuantityException();
		}
	}

	private Product getProductById(final CartItemAddRequest request) {
		return productRepository.findById(request.getProductId())
			.orElseThrow(ProductNotExistException::new);
	}
}
