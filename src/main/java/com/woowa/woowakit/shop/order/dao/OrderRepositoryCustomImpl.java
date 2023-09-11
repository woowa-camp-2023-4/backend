package com.woowa.woowakit.shop.order.dao;

import static com.woowa.woowakit.shop.order.domain.QOrder.*;

import java.util.List;

import com.woowa.woowakit.shop.order.domain.Order;
import com.woowa.woowakit.shop.order.domain.OrderStatus;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Order> findOrdersByMemberId(Long memberId, Long lastOrderId, int pageSize) {
		return jpaQueryFactory.selectFrom(order)
			.where(order.memberId.eq(memberId),
				cursor(lastOrderId),
				order.orderStatus.in(OrderStatus.PAYED, OrderStatus.CANCELED))
			.orderBy(order.id.desc())
			.limit(pageSize)
			.fetch();
	}

	private BooleanExpression cursor(Long lastOrderId) {
		if (lastOrderId == null) {
			return null;
		}

		return order.id.lt(lastOrderId);
	}
}
