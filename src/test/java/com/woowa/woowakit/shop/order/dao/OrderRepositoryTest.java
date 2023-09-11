package com.woowa.woowakit.shop.order.dao;

import com.woowa.woowakit.shop.auth.domain.EncodedPassword;
import com.woowa.woowakit.shop.auth.domain.Member;
import com.woowa.woowakit.shop.order.domain.Order;
import com.woowa.woowakit.shop.order.domain.OrderItem;
import com.woowa.woowakit.shop.order.domain.OrderRepository;
import com.woowa.woowakit.shop.order.fixture.OrderFixture;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderRepository 단위 테스트")
@DataJpaTest
@Import({QuerydslTestConfig.class, JpaConfig.class})
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
        order1.pay();

        Order order2 = OrderFixture.anOrder()
                .memberId(member.getId())
                .orderItems(List.of(orderItem2))
                .build();
        order2.pay();

        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        List<Order> result = orderRepository.findOrdersByMemberId(member.getId(), order2.getId() + 1L, 20);

        // then
        assertThat(result).hasSize(2);
    }
}
