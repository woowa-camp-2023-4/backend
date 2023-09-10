package com.woowa.woowakit.domains.order.domain;

import java.util.List;

import com.woowa.woowakit.domains.cart.domain.CartItemRepository;
import com.woowa.woowakit.domains.order.exception.OrderNotFoundException;
import com.woowa.woowakit.domains.product.domain.product.Product;
import com.woowa.woowakit.domains.product.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domains.auth.domain.AuthPrincipal;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPlaceService {

	private final OrderRepository orderRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	@Transactional
	@Counted("order.order")
	public void place(final AuthPrincipal authPrincipal, final Long orderId) {
		log.info("주문 수량 확보 및 장바구니 삭제 memberId: {} orderId: {}", authPrincipal.getId(), orderId);
		Order order = getOrderById(authPrincipal.getId(), orderId);

		order.place();
		subtractProductQuantity(order);
		deleteCartItems(order);
	}

	private void deleteCartItems(final Order order) {
		List<Long> productIds = order.collectProductIds();
		cartItemRepository.deleteAllByProductIdAndMemberId(order.getMemberId(), productIds);
		log.info("장바구니 상품 삭제 memberId: {} productIds: {}", order.getMemberId(), productIds);
	}

	private void subtractProductQuantity(final Order order) {
		List<Product> products = productRepository.findByIdsWithPessimistic(order.collectProductIds());
		QuantityOfProducts quantityData = QuantityOfProducts.from(order);

		for (Product product : products) {
			log.info("상품 {}의 수량을 {}만큼 감소시킵니다. 현재 상품 수량: {}",
				product.getId(), quantityData.getFrom(product), product.getQuantity().getValue());

			product.subtractQuantity(quantityData.getFrom(product));

			log.info("상품 {}의 수량이 {}로 변경되었습니다.", product.getId(), product.getQuantity().getValue());
		}
	}

	private Order getOrderById(final Long memberId, final Long orderId) {
		return orderRepository.findByIdAndMemberId(orderId, memberId)
			.orElseThrow(OrderNotFoundException::new);
	}
}
