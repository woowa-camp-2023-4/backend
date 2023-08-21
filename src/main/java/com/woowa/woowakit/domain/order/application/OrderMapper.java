package com.woowa.woowakit.domain.order.application;

import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.product.domain.product.Product;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order mapFrom(Long memberId, Product product, Long quantity) {
        OrderItem orderItem = OrderItem.of(
            product.getId(),
            product.getName().getName(),
            Image.from(product.getImageUrl().getPath()),
            product.getPrice().getPrice(),
            Quantity.from(quantity)
        );

        return Order.of(memberId, List.of(orderItem));
    }
}
