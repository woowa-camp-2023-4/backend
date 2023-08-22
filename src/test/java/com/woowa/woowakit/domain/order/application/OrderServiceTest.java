package com.woowa.woowakit.domain.order.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.auth.domain.EncodedPassword;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.auth.domain.MemberRepository;
import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateCartItemRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
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
	private CartItemRepository cartItemRepository;

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
			orderService.preOrder(AuthPrincipal.from(member), request);
		}

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

	@Test
	@DisplayName("장바구니 주문 테스트")
	void test2() {
		// given
		Member member = memberRepository.save(Member.of(
			"test1@test.com",
			EncodedPassword.from("test"),
			"test1"));

		Product product = productRepository.save(Product.builder()
			.price(ProductPrice.from(10000L))
			.quantity(Quantity.from(100))
			.imageUrl(ProductImage.from("/path"))
			.name(ProductName.from("된장 밀키트"))
			.status(ProductStatus.IN_STOCK)
			.build());

		CartItem cartItem = CartItem.of(member.getId(), product.getId(), 3);
		cartItemRepository.save(cartItem);

		PreOrderCreateCartItemRequest request = new PreOrderCreateCartItemRequest(cartItem.getId());
		PreOrderResponse preOrderResponse = orderService.preOrderCartItems(AuthPrincipal.from(member), List.of(request));

		// when
		OrderCreateRequest orderCreateRequest = OrderCreateRequest.of(preOrderResponse.getId(), "test");
		orderService.order(AuthPrincipal.from(member), orderCreateRequest);

		// then
		Product afterProduct = productRepository.findById(product.getId()).orElseThrow();
		assertThat(cartItemRepository.findCartItemByIdAndMemberId(member.getId(), cartItem.getId())).isNotPresent();
		assertThat(afterProduct.getQuantity()).isEqualTo(Quantity.from(97));
	}
}
