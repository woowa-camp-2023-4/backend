package com.woowa.woowakit.domain.order.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.auth.domain.MemberRepository;
import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.member.fixture.MemberFixture;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.OrderMapper;
import com.woowa.woowakit.domain.order.domain.handler.CartItemDeletionEventHandler;
import com.woowa.woowakit.domain.order.domain.handler.ProductQuantityEventHandler;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateCartItemRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.payment.domain.PaymentClient;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.fixture.ProductFixture;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;

@DisplayName("OrderService 단위 테스트")
@DataJpaTest
@Import({
	OrderService.class,
	QuerydslTestConfig.class,
	OrderMapper.class,
	CartItemDeletionEventHandler.class,
	ProductQuantityEventHandler.class,
	JpaConfig.class
})
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
	private PaymentClient paymentClient;

	@Test
	@DisplayName("장바구니 주문 테스트")
	void test1() {
		// given
		Member member = memberRepository.save(MemberFixture.anMember().build());

		Product product = productRepository.save(ProductFixture.anProduct().build());

		CartItem cartItem = CartItem.of(member.getId(), product.getId(), 3);
		cartItemRepository.save(cartItem);

		PreOrderCreateCartItemRequest request = new PreOrderCreateCartItemRequest(cartItem.getId());
		PreOrderResponse preOrderResponse = orderService.preOrderCartItems(AuthPrincipal.from(member),
			List.of(request));

		// when
		OrderCreateRequest orderCreateRequest = OrderCreateRequest.of(preOrderResponse.getId(), "test");
		orderService.order(AuthPrincipal.from(member), orderCreateRequest);

		// then
		Product afterProduct = productRepository.findById(product.getId()).orElseThrow();
		assertThat(cartItemRepository.findCartItemByIdAndMemberId(member.getId(), cartItem.getId())).isNotPresent();
		assertThat(afterProduct.getQuantity()).isEqualTo(Quantity.from(97));
	}
}
