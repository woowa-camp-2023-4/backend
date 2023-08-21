package com.woowa.woowakit.domain.cart.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.cart.domain.CartItemSpecification;
import com.woowa.woowakit.domain.cart.domain.CartItemValidator;
import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domain.model.Quantity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemService {

	private final CartItemRepository cartItemRepository;
	private final CartItemValidator cartItemValidator;

	@Transactional
	public CartItem addCartItem(final CartItemAddRequest request, final Long memberId) {
		CartItem cartItem = cartItemRepository.findCartItemByMemberIdAndProductId(memberId, request.getProductId())
			.orElse(CartItem.of(memberId, request.getProductId()));
		cartItem.addQuantity(Quantity.from(request.getQuantity()), cartItemValidator);

		return cartItemRepository.save(cartItem);
	}

	@Transactional
	public List<CartItemSpecification> readCartItem(final Long memberId) {
		return cartItemRepository.findCartItemByMemberId(memberId);
	}
}
