package com.woowa.woowakit.domain.order.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("select o from Order o join fetch o.orderItems where o.memberId = :memberId")
	List<Order> findAllByMemberId(@Param("memberId") Long memberId);

	@Query("select o from Order o join fetch o.orderItems where o.id = :id and o.memberId = :memberId")
	Optional<Order> findOrderById(@Param("id") Long id, @Param("memberId") Long memberId);

	Optional<Order> findByIdAndMemberId(Long orderId, Long memberId);
}
