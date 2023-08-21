package com.woowa.woowakit.domain.order.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.woowakit.domain.auth.annotation.Authenticated;
import com.woowa.woowakit.domain.auth.annotation.User;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.order.application.OrderService;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/pre")
	@User
	public ResponseEntity<PreOrderResponse> createPreOrder(
		@Authenticated final AuthPrincipal authPrincipal,
		@Valid @RequestBody final PreOrderCreateRequest request
	) {
		PreOrderResponse preOrderResponse = orderService.preOrder(authPrincipal.getId(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(preOrderResponse);
	}

	@User
	@PostMapping()
	public ResponseEntity<Long> createOrder(
		@Authenticated final AuthPrincipal authPrincipal,
		@Valid @RequestBody final OrderCreateRequest request
	) {
		Long orderId = orderService.order(authPrincipal.getId(), request);
		return ResponseEntity.status(HttpStatus.OK).body(orderId);
	}
}
