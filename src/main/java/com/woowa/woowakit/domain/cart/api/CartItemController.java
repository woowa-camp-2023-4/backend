package com.woowa.woowakit.domain.cart.api;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.woowakit.domain.auth.annotation.Authenticated;
import com.woowa.woowakit.domain.auth.annotation.User;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.cart.application.CartItemService;
import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemSpecification;
import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domain.cart.dto.CartItemUpdateQuantityRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {

	private final CartItemService cartItemService;

	@User
	@GetMapping
	public ResponseEntity<List<CartItemSpecification>> readCartItems(@Authenticated final AuthPrincipal authPrincipal) {
		List<CartItemSpecification> responses = cartItemService.readCartItem(authPrincipal.getId());
		return ResponseEntity.ok(responses);
	}

	@User
	@PostMapping
	public ResponseEntity<Void> addCartItem(
		@Valid @RequestBody final CartItemAddRequest request,
		@Authenticated final AuthPrincipal authPrincipal
	) {
		CartItem cartItem = cartItemService.addCartItem(request, authPrincipal.getId());
		return ResponseEntity.created(URI.create("/cart-items/" + cartItem.getId())).build();
	}

	@User
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCartItem(
		@PathVariable("id") final Long cartItemId,
		@Authenticated final AuthPrincipal authPrincipal
	) {
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
		cartItemService.updateQuantity(cartItemId, request, authPrincipal.getId());
		return ResponseEntity.ok().build();
	}
}
