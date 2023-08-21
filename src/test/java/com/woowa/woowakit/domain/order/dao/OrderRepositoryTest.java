package com.woowa.woowakit.domain.order.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.woowa.woowakit.domain.auth.domain.EncodedPassword;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.fixture.OrderFixture;
import com.woowa.woowakit.global.config.QuerydslTestConfig;

@DisplayName("OrderRepository 단위 테스트")
@DataJpaTest
@Import(QuerydslTestConfig.class)
class OrderRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@DisplayName("회원의 주문 상세 정보를 확인한다.")
	void findById() {
		// given
		Member member = Member.of("tamtam@hello.com", EncodedPassword.from("happytamtam"), "탐탐");
		entityManager.persist(member);

		OrderItem orderItem1 = OrderFixture.anOrderItem()
			.productId(1L)
			.build();

		OrderItem orderItem2 = OrderFixture.anOrderItem()
			.productId(2L)
			.build();

		Order order = OrderFixture.anOrder()
			.memberId(member.getId())
			.orderItems(List.of(orderItem1, orderItem2))
			.build();

		Long orderId = orderRepository.save(order).getId();

		// when
		Order result = orderRepository.findOrderById(orderId, member.getId()).get();

		// then
		assertThat(result).extracting(Order::getId).isEqualTo(orderId);
		assertThat(result.getOrderItems()).hasSize(2);
		assertThat(result.getOrderItems().get(0)).extracting("id").isEqualTo(orderItem1.getId());
		assertThat(result.getOrderItems().get(1)).extracting("id").isEqualTo(orderItem2.getId());
	}

	@Test
	@DisplayName("회원의 주문 정보들을 확인한다.")
	void findAllByMemberId() {
		// given
		Member member = Member.of("tamtam@hello.com", EncodedPassword.from("happytamtam"), "탐탐");
		entityManager.persist(member);

		OrderItem orderItem1 = OrderFixture.anOrderItem()
			.productId(1L)
			.build();

		OrderItem orderItem2 = OrderFixture.anOrderItem()
			.productId(2L)
			.build();

		Order order1 = OrderFixture.anOrder()
			.memberId(member.getId())
			.orderItems(List.of(orderItem1))
			.build();

		Order order2 = OrderFixture.anOrder()
			.memberId(member.getId())
			.orderItems(List.of(orderItem2))
			.build();

		orderRepository.save(order1);
		orderRepository.save(order2);

		// when
		List<Order> result = orderRepository.findAllByMemberId(member.getId());

		// then
		assertThat(result).hasSize(2);
	}
}
