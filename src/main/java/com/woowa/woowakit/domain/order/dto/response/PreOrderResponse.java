package com.woowa.woowakit.domain.order.dto.response;

import com.woowa.woowakit.domain.order.domain.Order;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PreOrderResponse {

    private Long id;
    private List<OrderItemResponse> orderItems;
    private String uuid;

    public static PreOrderResponse of(final Long id, final List<OrderItemResponse> orderItems,
        final String uuid) {
        return new PreOrderResponse(id, orderItems, uuid);
    }

    //from order
    public static PreOrderResponse from(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
            .map(OrderItemResponse::from)
            .collect(Collectors.toUnmodifiableList());

        return PreOrderResponse.of(
            order.getId(),
            orderItemResponses,
            order.getUuid()
        );
    }
}
