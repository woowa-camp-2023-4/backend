package com.woowa.woowakit.domain.order.api;

import java.net.URI;
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
import com.woowa.woowakit.domain.order.dto.request.OrderPayRequest;
import com.woowa.woowakit.domain.order.dto.request.OrderSearchRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domain.order.dto.response.OrderResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@User
	@PostMapping
	public ResponseEntity<OrderResponse> create(
		@Authenticated final AuthPrincipal authPrincipal,
		@Valid @RequestBody final List<OrderCreateRequest> request
	) {
		OrderResponse response = orderService.create(authPrincipal, request);
		return ResponseEntity.created(URI.create("/orders/" + response.getId())).body(response);
	}

	@User
	@PostMapping("/{id}/pay")
	public ResponseEntity<Void> pay(
		@Authenticated final AuthPrincipal authPrincipal,
		@PathVariable final Long id,
		@Valid @RequestBody final OrderPayRequest request
	) {
		orderService.pay(authPrincipal, id, request);
		return ResponseEntity.status(HttpStatus.OK).build();
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
