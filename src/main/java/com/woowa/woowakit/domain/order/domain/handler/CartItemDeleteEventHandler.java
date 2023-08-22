package com.woowa.woowakit.domain.order.domain.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartItemDeleteEventHandler {

	private final CartItemRepository cartItemRepository;

	@Order(1)
	@Transactional
	@EventListener
	public void handle(final OrderCompleteEvent event) {
		deleteCartItems(event);
	}

	private void deleteCartItems(final OrderCompleteEvent event) {
		List<Long> productIds = event.getOrderItem().stream()
			.map(OrderItem::getProductId)
			.collect(Collectors.toUnmodifiableList());
		
		cartItemRepository.deleteAllByProductIdAndMemberId(event.getOrder().getMemberId(), productIds);
	}
}
