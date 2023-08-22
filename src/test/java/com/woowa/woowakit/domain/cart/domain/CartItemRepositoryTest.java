package com.woowa.woowakit.domain.cart.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.woowa.woowakit.domain.auth.domain.EncodedPassword;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.global.config.QuerydslTestConfig;

@DisplayName("CartItemRepository 단위 테스트")
@DataJpaTest
@Import(QuerydslTestConfig.class)
class CartItemRepositoryTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Test
	@DisplayName("장바구니 id와 회원 id를 바탕으로 장바구니와 상품을 Projection한 클래스를 찾는다.")
	void findCartItemByIdAndMemberId() {
		// given
		Member member = Member.of("jiwonjjang@abcd.com", EncodedPassword.from("jiwonjjang"), "탐탐");
		Product product = Product.of("상품1", 15000L, "product.img");

		entityManager.persist(member);
		entityManager.persist(product);

		CartItem cartItem = CartItem.of(member.getId(), product.getId());

		entityManager.persist(cartItem);

		// when
		CartItemSpecification result = cartItemRepository.findCartItemByIdAndMemberId(
			member.getId(), cartItem.getId()).get();

		// then
		assertThat(result).extracting(CartItemSpecification::getProductId).isEqualTo(product.getId());
	}

	@Test
	@DisplayName("장바구니 id와 회원 id를 바탕으로 장바구니를 삭제한다.")
	void deleteAll() {
		// given
		Member member = Member.of("jiwonjjang@abcd.com", EncodedPassword.from("jiwonjjang"), "탐탐");
		Product product1 = Product.of("상품1", 15000L, "product.img");
		Product product2 = Product.of("상품2", 15000L, "product.img");
		Product product3 = Product.of("상품3", 15000L, "product.img");

		entityManager.persist(member);
		entityManager.persist(product1);
		entityManager.persist(product2);
		entityManager.persist(product3);

		CartItem cartItem1 = CartItem.of(member.getId(), product1.getId());
		CartItem cartItem2 = CartItem.of(member.getId(), product2.getId());
		CartItem cartItem3 = CartItem.of(member.getId(), product3.getId());

		entityManager.persist(cartItem1);
		entityManager.persist(cartItem2);
		entityManager.persist(cartItem3);

		// when
		cartItemRepository.deleteAllByProductIdAndMemberId(member.getId(),
			List.of(product1.getId(), product2.getId(), product3.getId()));

		// then
		List<CartItemSpecification> result = cartItemRepository.findCartItemByMemberId(member.getId());
		assertThat(result).isEmpty();
	}

}
