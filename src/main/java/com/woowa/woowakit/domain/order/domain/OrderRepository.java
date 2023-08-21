package com.woowa.woowakit.domain.order.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByIdAndMemberId(Long orderId, Long memberId);
}
