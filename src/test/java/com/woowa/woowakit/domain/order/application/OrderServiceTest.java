package com.woowa.woowakit.domain.order.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.auth.domain.EncodedPassword;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.auth.domain.MemberRepository;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.OrderItemStock;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.stock.Stock;
import com.woowa.woowakit.domain.product.domain.stock.StockRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("OrderService 단위테스트")
@DataJpaTest
@Import({OrderService.class, OrderMapper.class})
class OrderServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 생성")
    void test1() {
        // given
        Member member = memberRepository.save(Member.of(
            "test@test.com",
            EncodedPassword.from("test"),
            "test"));

        Product product = productRepository.save(Product.of("test", 1000L, "image"));

        Stock stock1 = stockRepository.save(Stock.of(LocalDate.now().plusDays(100L), product));
        stock1.addQuantity(Quantity.from(10L));
        Stock stock2 = stockRepository.save(Stock.of(LocalDate.now().plusDays(200L), product));
        stock2.addQuantity(Quantity.from(20L));

        PreOrderResponse preOrderResponse = orderService.preOrder(AuthPrincipal.from(member),
            PreOrderCreateRequest.of(product.getId(), 20L));

        // when
        orderService.order(AuthPrincipal.from(member),
            OrderCreateRequest.of(preOrderResponse.getId(), "paymentKey"));

        // then
        Order order = orderRepository.findById(preOrderResponse.getId()).get();
        assertThat(order)
            .extracting(Order::getOrderItems).asList().hasSize(1);
        assertThat(order.getOrderItems().get(0))
            .extracting(OrderItem::getOrderItemStocks).asList().hasSize(2);

        assertThat(order.getOrderItems().get(0).getOrderItemStocks().get(0))
            .extracting(OrderItemStock::getQuantity).isEqualTo(Quantity.from(10L));
        assertThat(order.getOrderItems().get(0).getOrderItemStocks().get(1))
            .extracting(OrderItemStock::getQuantity).isEqualTo(Quantity.from(10L));

    }
}
