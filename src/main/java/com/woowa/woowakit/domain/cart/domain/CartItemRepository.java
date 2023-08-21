package com.woowa.woowakit.domain.cart.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findCartItemByMemberIdAndProductId(Long memberId, Long productId);

	@Query(
		" select new com.woowa.woowakit.domain.cart.domain.CartItemSpecification(c.quantity , p.price,p.name, p.imageUrl,p.id)"
			+ "  from CartItem c"
			+ "  join Product p on p.id = c.productId "
			+ "  where c.memberId = :memberId")
	List<CartItemSpecification> findCartItemByMemberId(@Param("memberId") Long memberId);
}
