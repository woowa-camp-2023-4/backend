package com.woowa.woowakit.shop.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.woowa.woowakit.shop.product.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.woowa.woowakit.shop.auth.domain.AuthPrincipal;
import com.woowa.woowakit.shop.auth.domain.Member;
import com.woowa.woowakit.shop.auth.domain.MemberRepository;
import com.woowa.woowakit.shop.cart.domain.CartItemRepository;
import com.woowa.woowakit.shop.member.fixture.MemberFixture;
import com.woowa.woowakit.shop.model.Quantity;
import com.woowa.woowakit.shop.order.domain.PaymentClient;
import com.woowa.woowakit.shop.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.shop.order.dto.request.OrderPayRequest;
import com.woowa.woowakit.shop.product.domain.product.Product;
import com.woowa.woowakit.shop.product.domain.product.ProductRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@DisplayName("OrderService 동시성 테스트")
class OrderServiceConcurrencyTest {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderService orderService;

	@MockBean
	private PaymentClient paymentClient;

	@Test
	@DisplayName("주문 동시성 테스트")
	void test1() throws Exception {
		// given
		Member member = memberRepository.save(MemberFixture.anMember().build());

		Product product = productRepository.save(ProductFixture.anProduct().build());

		int threadCount = 10;
		for (int i = 0; i < threadCount; i++) {
			List<OrderCreateRequest> request = List.of(OrderCreateRequest.of(product.getId(), 10L));
			orderService.create(AuthPrincipal.from(member), request);
		}
		when(paymentClient.validatePayment(any(), any(), any())).thenReturn(Mono.empty());

		// when
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			long orderId = i + 1;
			OrderPayRequest request = OrderPayRequest.of("test");

			executorService.submit(() -> {
				try {
					orderService.pay(AuthPrincipal.from(member), orderId, request);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();
		productRepository.flush();

		// then
		Product afterProduct = productRepository.findById(product.getId()).orElseThrow();
		assertThat(afterProduct.getQuantity()).isEqualTo(Quantity.from(0));
	}
}
