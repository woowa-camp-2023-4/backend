package com.woowa.woowakit.domain.order.dao;

import java.util.List;

import com.woowa.woowakit.domain.order.domain.Order;

public interface OrderRepositoryCustom {

	List<Order> findOrdersByMemberId(Long memberId, Long lastOrderId, int pageSize);
}
