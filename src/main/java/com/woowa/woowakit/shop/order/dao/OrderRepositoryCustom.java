package com.woowa.woowakit.shop.order.dao;

import java.util.List;

import com.woowa.woowakit.shop.order.domain.Order;

public interface OrderRepositoryCustom {

	List<Order> findOrdersByMemberId(Long memberId, Long lastOrderId, int pageSize);
}
