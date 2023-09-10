package com.woowa.woowakit.domains.order.dao;

import java.util.List;

import com.woowa.woowakit.domains.order.domain.Order;

public interface OrderRepositoryCustom {

	List<Order> findOrdersByMemberId(Long memberId, Long lastOrderId, int pageSize);
}
