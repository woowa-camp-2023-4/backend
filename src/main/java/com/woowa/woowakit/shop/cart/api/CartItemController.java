package com.woowa.woowakit.shop.cart.api;

import com.woowa.woowakit.shop.auth.annotation.Authenticated;
import com.woowa.woowakit.shop.auth.annotation.User;
import com.woowa.woowakit.shop.auth.domain.AuthPrincipal;
import com.woowa.woowakit.shop.cart.application.CartItemService;
import com.woowa.woowakit.shop.cart.domain.CartItem;
import com.woowa.woowakit.shop.cart.domain.CartItemSpecification;
import com.woowa.woowakit.shop.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.shop.cart.dto.CartItemUpdateQuantityRequest;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {

	private final CartItemService cartItemService;

	@User
	@GetMapping
	public ResponseEntity<List<CartItemSpecification>> readCartItems(
		@Authenticated final AuthPrincipal authPrincipal) {
		log.info("[Request] 장바구니 조회 : memberId = {}", authPrincipal.getId());
		List<CartItemSpecification> responses = cartItemService.readCartItem(authPrincipal.getId());
		return ResponseEntity.ok(responses);
	}

	@User
	@PostMapping
	public ResponseEntity<Void> addCartItem(
		@Valid @RequestBody final CartItemAddRequest request,
		@Authenticated final AuthPrincipal authPrincipal
	) {
		log.info("[Request] 장바구니 추가 : memberId = {}, productId = {}, quantity = {}",
			authPrincipal.getId(), request.getProductId(), request.getQuantity());
		CartItem cartItem = cartItemService.addCartItem(request, authPrincipal.getId());
		return ResponseEntity.created(URI.create("/cart-items/" + cartItem.getId())).build();
	}

	@User
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCartItem(
		@PathVariable("id") final Long cartItemId,
		@Authenticated final AuthPrincipal authPrincipal
	) {
		log.info("[Request] 장바구니 삭제 : memberId = {}, cartItemId = {}", authPrincipal.getId(),
			cartItemId);
		cartItemService.deleteCartItems(cartItemId, authPrincipal.getId());
		return ResponseEntity.ok().build();
	}

	@User
	@PatchMapping("/{id}/quantity")
	public ResponseEntity<Void> updateQuantity(
		@Valid @RequestBody final CartItemUpdateQuantityRequest request,
		@PathVariable("id") final Long cartItemId,
		@Authenticated final AuthPrincipal authPrincipal
	) {
		log.info("[Request] 장바구니 수량 업데이트 : memberId = {}, cartItemId = {}, quantity = {}",
			authPrincipal.getId(), cartItemId, request.getQuantity());
		cartItemService.updateQuantity(cartItemId, request, authPrincipal.getId());
		return ResponseEntity.ok().build();
	}
}

