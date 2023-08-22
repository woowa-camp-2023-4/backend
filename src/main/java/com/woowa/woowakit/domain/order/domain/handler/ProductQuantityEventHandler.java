package com.woowa.woowakit.domain.order.domain.handler;

import com.woowa.woowakit.domain.cart.exception.ProductNotExistException;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductQuantityEventHandler {

    private final ProductRepository productRepository;

    @Order(0)
    @Transactional
    @EventListener
    public void handle(final OrderCompleteEvent event) {
        subtractProductQuantity(event);
    }

    private void subtractProductQuantity(final OrderCompleteEvent event) {
        List<OrderItem> orderItems = event.getOrderItem();
        for (OrderItem orderItem : orderItems) {
            Product product = getProductWithPessimistic(orderItem);
            product.subtractQuantity(orderItem.getQuantity());
        }
    }

    private Product getProductWithPessimistic(final OrderItem orderItem) {
        return productRepository.findByIdWithPessimistic(orderItem.getProductId())
            .orElseThrow(ProductNotExistException::new);
    }
}
