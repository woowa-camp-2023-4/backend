package com.woowa.woowakit.domain.cart.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.woowakit.domain.auth.annotation.Authenticated;
import com.woowa.woowakit.domain.auth.annotation.User;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.cart.application.CartItemService;
import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {

	private final CartItemService cartItemService;

	@User
	@PostMapping
	public ResponseEntity<Void> addCartItem(
		@Valid @RequestBody final CartItemAddRequest request,
		@Authenticated final AuthPrincipal authPrincipal
	) {
		CartItem cartItem = cartItemService.addCartItem(request, authPrincipal.getId());
		return ResponseEntity.created(URI.create("/cart-items/" + cartItem.getId())).build();
	}
}
