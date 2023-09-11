package com.woowa.woowakit.shop.cart.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findCartItemByMemberIdAndProductId(Long memberId, Long productId);

	@Query(
		" select new com.woowa.woowakit.shop.cart.domain.CartItemSpecification(c.quantity, p.price, p.name, p.imageUrl, p.id, c.id)"
			+ "  from CartItem c"
			+ "  join Product p on p.id = c.productId "
			+ "  where c.memberId = :memberId")
	List<CartItemSpecification> findCartItemByMemberId(@Param("memberId") Long memberId);

	@Query(
		"select new com.woowa.woowakit.shop.cart.domain.CartItemSpecification(c.quantity, p.price, p.name, p.imageUrl, p.id, c.id)"
			+ " from CartItem c "
			+ " join Product p on p.id = c.productId "
			+ " where c.memberId = :memberId and c.id = :id")
	Optional<CartItemSpecification> findCartItemByIdAndMemberId(@Param("memberId") Long memberId, @Param("id") Long id);

	@Modifying
	@Query("delete from CartItem c where c.productId in :productIds and c.memberId = :memberId")
	void deleteAllByProductIdAndMemberId(@Param("memberId") Long memberId, @Param("productIds") List<Long> productIds);

	@Modifying
	@Query("delete from CartItem c where c.id in :cartItemIds and c.memberId = :memberId")
	void deleteCartItems(@Param("memberId") Long memberId, @Param("cartItemIds") List<Long> cartItemIds);
}
