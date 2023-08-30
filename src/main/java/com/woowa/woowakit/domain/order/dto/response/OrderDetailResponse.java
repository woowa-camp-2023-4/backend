package com.woowa.woowakit.domain.order.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.woowa.woowakit.domain.order.domain.Order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderDetailResponse {

	private Long orderId;
	private String orderStatus;
	private Long totalPrice;
	private String uuid;
	private List<OrderItemResponse> orderItems;

	public static OrderDetailResponse from(final Order order) {
		return new OrderDetailResponse(
			order.getId(),
			order.getOrderStatus().name(),
			order.getTotalPrice().getValue(),
			order.getUuid(),
			order.getOrderItems().stream()
				.map(OrderItemResponse::from)
				.collect(Collectors.toUnmodifiableList())
		);
	}

	public static List<OrderDetailResponse> listOf(final List<Order> orders) {
		return orders.stream()
			.map(OrderDetailResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
