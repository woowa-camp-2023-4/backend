package com.woowa.woowakit.domain.order.domain.validator;

import com.woowa.woowakit.domain.order.domain.Order;

public interface OrderValidator {

    void validate(Long requestMemberId, Order order);
}
