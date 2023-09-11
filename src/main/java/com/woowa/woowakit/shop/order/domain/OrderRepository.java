package com.woowa.woowakit.shop.order.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woowa.woowakit.shop.order.dao.OrderRepositoryCustom;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

	@Query("select o from Order o join fetch o.orderItems where o.id = :id and o.memberId = :memberId")
	Optional<Order> findOrderById(@Param("id") Long id, @Param("memberId") Long memberId);

	Optional<Order> findByIdAndMemberId(Long orderId, Long memberId);
}
