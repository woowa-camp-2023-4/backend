package com.woowa.woowakit.domain.order.application;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.woowa.woowakit.domain.auth.domain.EncodedPassword;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.auth.domain.MemberRepository;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.payment.domain.PaymentService;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductImage;
import com.woowa.woowakit.domain.product.domain.product.ProductName;
import com.woowa.woowakit.domain.product.domain.product.ProductPrice;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.product.ProductStatus;

@SpringBootTest
class OrderServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderService orderService;

	@MockBean
	private PaymentService paymentService;

	@Test
	@DisplayName("주문 동시성 테스트")
	void test1() throws Exception {
		// given
		Member member = memberRepository.save(Member.of(
			"test@test.com",
			EncodedPassword.from("test"),
			"test"));

		Product product = productRepository.save(Product.builder()
			.price(ProductPrice.from(10000L))
			.quantity(Quantity.from(100))
			.imageUrl(ProductImage.from("/path"))
			.name(ProductName.from("된장 밀키트"))
			.status(ProductStatus.IN_STOCK)
			.build());

		int threadCount = 10;
		for (int i = 0; i < threadCount; i++) {
			PreOrderCreateRequest request = PreOrderCreateRequest.of(product.getId(), 10L);
			orderService.preOrder(member.getId(), request);
		}

		// when
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			OrderCreateRequest request = OrderCreateRequest.of((long)(i + 1), "test");
			executorService.submit(() -> {
				try {
					orderService.order(member.getId(), request);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();
		productRepository.flush();

		// then
		Product afterProduct = productRepository.findById(product.getId()).orElseThrow();
		Assertions.assertThat(afterProduct.getQuantity()).isEqualTo(Quantity.from(0));
	}
}
