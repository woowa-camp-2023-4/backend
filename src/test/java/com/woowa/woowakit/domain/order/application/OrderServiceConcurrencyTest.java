package com.woowa.woowakit.domain.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.auth.domain.MemberRepository;
import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.member.fixture.MemberFixture;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.PaymentClient;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.fixture.ProductFixture;

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
			PreOrderCreateRequest request = PreOrderCreateRequest.of(product.getId(), 10L);
			orderService.preOrder(AuthPrincipal.from(member), request);
		}
		when(paymentClient.validatePayment(any(), any(), any())).thenReturn(Mono.empty());

		// when
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			OrderCreateRequest request = OrderCreateRequest.of((long)(i + 1), "test");
			executorService.submit(() -> {
				try {
					orderService.order(AuthPrincipal.from(member), request);
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
