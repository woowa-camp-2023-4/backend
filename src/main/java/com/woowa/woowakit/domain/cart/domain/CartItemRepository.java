package com.woowa.woowakit.domain.cart.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findCartItemByMemberIdAndProductId(Long memberId, Long productId);
}
