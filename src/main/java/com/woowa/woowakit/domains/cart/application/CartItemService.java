package com.woowa.woowakit.domains.cart.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domains.cart.domain.CartItem;
import com.woowa.woowakit.domains.cart.domain.CartItemRepository;
import com.woowa.woowakit.domains.cart.domain.CartItemSpecification;
import com.woowa.woowakit.domains.cart.domain.CartItemValidator;
import com.woowa.woowakit.domains.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domains.cart.dto.CartItemUpdateQuantityRequest;
import com.woowa.woowakit.domains.cart.exception.CartItemNotExistException;
import com.woowa.woowakit.domains.cart.exception.NotMyCartItemException;
import com.woowa.woowakit.domains.model.Quantity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		log.info("CartItemService.addCartItem() 실행 전: cartItemId = {}, quantity = {}, addQuantity = {}", cartItem.getId(), cartItem.getQuantity().getValue(), request.getQuantity());
		cartItem.addQuantity(Quantity.from(request.getQuantity()), cartItemValidator);
		log.info("CartItemService.addCartItem() 실행 후: cartItemId = {}, quantity = {}", cartItem.getId(), cartItem.getQuantity().getValue());

		return cartItemRepository.save(cartItem);
	}

	@Transactional
	public List<CartItemSpecification> readCartItem(final Long memberId) {
		return cartItemRepository.findCartItemByMemberId(memberId);
	}

	@Transactional
	public void deleteCartItems(final Long cartItemId, final Long memberId) {
		cartItemRepository.deleteCartItems(memberId, List.of(cartItemId));
	}

	@Transactional
	public void updateQuantity(final Long cartItemId, final CartItemUpdateQuantityRequest request, final Long memberId) {
		CartItem cartItem = getCartItem(cartItemId);

		log.info("CartItemService.updateQuantity() 실행 전: cartItemId = {}, quantity = {}, addQuantity = {}", cartItem.getId(), cartItem.getQuantity().getValue(), request.getQuantity());

		if (!cartItem.isMyCartItem(memberId)) {
			throw new NotMyCartItemException();
		}

		cartItem.updateQuantity(request.getQuantity(), cartItemValidator);

		log.info("CartItemService.updateQuantity() 실행 후: cartItemId = {}, quantity = {}", cartItem.getId(), cartItem.getQuantity().getValue());
	}

	private CartItem getCartItem(final Long cartItemId) {
		return cartItemRepository.findById(cartItemId)
			.orElseThrow(CartItemNotExistException::new);
	}
}
