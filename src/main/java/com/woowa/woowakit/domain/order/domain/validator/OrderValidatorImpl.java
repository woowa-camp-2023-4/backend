package com.woowa.woowakit.domain.order.domain.validator;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.exception.NotMyOrderException;
import com.woowa.woowakit.domain.order.exception.ProductNotFoundException;
import com.woowa.woowakit.domain.order.exception.QuantityNotEnoughException;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidatorImpl implements OrderValidator {

    private final ProductRepository productRepository;

    public void validate(Long requestMemberId, Order order) {
        validateSameUser(requestMemberId, order);
        validateEnoughProductQuantity(order.getOrderItems().get(0));
    }

    void validateSameUser(Long requestMemberId, Order order) {
        if (!order.isSameUser(requestMemberId)) {
            throw new NotMyOrderException();
        }
    }

    void validateEnoughProductQuantity(OrderItem orderItem) {
        validateEnoughProductQuantity(orderItem, getProductQuantity(orderItem));
    }

    private Quantity getProductQuantity(OrderItem orderItem) {
        return productRepository.findQuantityCountById(
                orderItem.getProductId())
            .orElseThrow(ProductNotFoundException::new);
    }

    void validateEnoughProductQuantity(OrderItem orderItem, Quantity productQuantity) {
        if (productQuantity.smallerThan(orderItem.getQuantity())) {
            throw new QuantityNotEnoughException();
        }
    }
}
