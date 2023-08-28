package com.woowa.woowakit.domain.order.domain.handler;

import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartItemDeletionEventHandler {

	private final CartItemRepository cartItemRepository;

	@Transactional
	public void handle(final OrderCompleteEvent event) {
		deleteCartItems(event);
	}

	private void deleteCartItems(final OrderCompleteEvent event) {
		List<Long> productIds = event.getOrderItem().stream()
			.map(OrderItem::getProductId)
			.collect(Collectors.toUnmodifiableList());

		cartItemRepository.deleteAllByProductIdAndMemberId(event.getOrder().getMemberId(),
			productIds);
		log.info("장바구니 상품 삭제 memberId: {} productIds: {}", event.getOrder().getMemberId(),
			productIds);
	}
}
