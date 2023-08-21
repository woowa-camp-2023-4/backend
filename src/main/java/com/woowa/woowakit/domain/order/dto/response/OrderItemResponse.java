package com.woowa.woowakit.domain.order.dto.response;

import com.woowa.woowakit.domain.order.domain.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private Long quantity;

    public static OrderItemResponse of(final Long id, final Long productId, final Long quantity) {
        return new OrderItemResponse(id, productId, quantity);
    }

    public static OrderItemResponse from(final OrderItem orderItem) {
        return OrderItemResponse.of(
            orderItem.getId(),
            orderItem.getProductId(),
            orderItem.getQuantity().getValue()
        );
    }
}
