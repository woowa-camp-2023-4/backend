package com.woowa.woowakit.domains.order.fixture;

import com.woowa.woowakit.domains.model.Image;
import com.woowa.woowakit.domains.model.Money;
import com.woowa.woowakit.domains.model.Quantity;
import com.woowa.woowakit.domains.order.domain.Order;
import com.woowa.woowakit.domains.order.domain.Order.OrderBuilder;
import com.woowa.woowakit.domains.order.domain.OrderItem;
import com.woowa.woowakit.domains.order.domain.OrderItem.OrderItemBuilder;
import java.util.List;

public class OrderFixture {

    public static OrderItemBuilder anOrderItem() {
        return OrderItem.builder()
            .image(Image.from("image"))
            .name("name")
            .price(Money.from(1000L))
            .productId(1L)
            .quantity(Quantity.from(1L));
    }

    public static OrderBuilder anOrder() {
        return Order.builder()
            .memberId(1L)
            .orderItems(List.of(anOrderItem().build(),
                anOrderItem()
                    .productId(2L)
                    .name("해산물 밀키트")
                    .price(Money.from(2000L)).build()));
    }
}
