package com.woowa.woowakit.domain.order.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.woowakit.domain.auth.annotation.Authenticated;
import com.woowa.woowakit.domain.auth.annotation.User;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.order.application.OrderService;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.OrderSearchRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateCartItemRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
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
		PreOrderResponse preOrderResponse = orderService.preOrder(authPrincipal, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(preOrderResponse);
	}

	@PostMapping("/pre-cart-item")
	@User
	public ResponseEntity<PreOrderResponse> createPreOrderByCartItems(
		@Authenticated final AuthPrincipal authPrincipal,
		@Valid @RequestBody final List<PreOrderCreateCartItemRequest> requests
	) {
		PreOrderResponse preOrderResponse = orderService.preOrderCartItems(authPrincipal, requests);
		return ResponseEntity.status(HttpStatus.CREATED).body(preOrderResponse);
	}

	@User
	@PostMapping
	public ResponseEntity<Long> createOrder(
		@Authenticated final AuthPrincipal authPrincipal,
		@Valid @RequestBody final OrderCreateRequest request
	) {
		Long orderId = orderService.pay(authPrincipal, request);
		return ResponseEntity.status(HttpStatus.OK).body(orderId);
	}

	@User
	@GetMapping("/{id}")
	public ResponseEntity<OrderDetailResponse> getOrderDetail(
		@Authenticated final AuthPrincipal authPrincipal,
		@PathVariable final Long id
	) {
		return ResponseEntity.ok(orderService.findOrderByOrderIdAndMemberId(authPrincipal, id));
	}

	@User
	@GetMapping
	public ResponseEntity<List<OrderDetailResponse>> getOrderDetail(
		@Authenticated final AuthPrincipal authPrincipal,
		@ModelAttribute @Valid final OrderSearchRequest request
	) {
		return ResponseEntity.ok(orderService.findAllOrderByMemberId(authPrincipal, request));
	}
}
