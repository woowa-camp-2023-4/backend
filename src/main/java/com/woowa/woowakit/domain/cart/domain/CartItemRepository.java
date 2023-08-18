package com.woowa.woowakit.domain.cart.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findAllByMemberId(Long productId);
}
