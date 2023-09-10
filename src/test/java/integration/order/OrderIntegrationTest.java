package integration.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.woowa.woowakit.domains.order.domain.OrderStatus;
import com.woowa.woowakit.domains.order.domain.PaymentClient;
import com.woowa.woowakit.domains.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domains.order.dto.response.OrderResponse;
import com.woowa.woowakit.domains.product.domain.product.ProductStatus;
import com.woowa.woowakit.global.error.ErrorResponse;

import integration.IntegrationTest;
import integration.helper.CommonRestAssuredUtils;
import integration.helper.MemberHelper;
import integration.helper.OrderHelper;
import integration.helper.ProductHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import reactor.core.publisher.Mono;

@DisplayName("주문 통합 테스트")
class OrderIntegrationTest extends IntegrationTest {

	@MockBean
	private PaymentClient paymentClient;

	@Test
	@DisplayName("주문을 생성한다")
	void createOrder() {
		// given
		Long orderId1 = ProductHelper.createProductAndSetUp();
		Long orderId2 = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();

		// when
		ExtractableResponse<Response> response = OrderHelper.createOrder(
			OrderHelper.createOrderRequests(orderId1, orderId2), accessToken);

		// then
		OrderResponse orderResponse = response.as(OrderResponse.class);
		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(orderResponse).extracting(OrderResponse::getId, OrderResponse::getUuid).isNotNull();
		assertThat(orderResponse).extracting(OrderResponse::getOrderItems).asList().hasSize(2);
	}

	@Test
	@DisplayName("상품 구매가 불가능하면 주문 생성에 실패한다")
	void createOrderFail() {
		// given
		Long orderId = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();

		// when
		ExtractableResponse<Response> response = OrderHelper.createOrder(
			OrderHelper.createOrderRequest(orderId, 11L), accessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(400);
		ErrorResponse errorResponse = response.as(ErrorResponse.class);
		assertThat(errorResponse).extracting("message")
			.isEqualTo("상품의 재고가 부족합니다");
	}

	@Test
	@DisplayName("상품이 구매 불가능 상태일 떄 주문 생성에 실패한다.")
	void createOrderOnSaleFail() {
		// given
		Long orderId = ProductHelper.createProductAndSetUp(20L, ProductStatus.STOPPED);
		String accessToken = MemberHelper.signUpAndLogIn();

		// when
		ExtractableResponse<Response> response = OrderHelper.createOrder(
			OrderHelper.createOrderRequest(orderId, 11L), accessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(400);
		ErrorResponse errorResponse = response.as(ErrorResponse.class);
		assertThat(errorResponse)
			.extracting("message")
			.isEqualTo("판매 중이 아닌 상품입니다.");
	}

	@Test
	@DisplayName("주문을 결제한다")
	void pay() {
		// given
		Long productId = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();
		Long orderId = OrderHelper.createOrderAndGetId(productId, accessToken);
		Long beforeProductQuantity = ProductHelper.getProductDetail(productId).getQuantity();

		when(paymentClient.validatePayment(any(), any(), any())).thenReturn(Mono.empty());

		//when
		ExtractableResponse<Response> response = OrderHelper.payOrder(
			OrderHelper.createOrderPayRequest(), orderId, accessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(200);
		Long afterProductQuantity = ProductHelper.getProductDetail(productId).getQuantity();
		assertThat(afterProductQuantity).isEqualTo(beforeProductQuantity - 1);

		OrderDetailResponse orderResponse = OrderHelper.getOrder(orderId, accessToken);
		assertThat(orderResponse).extracting("orderStatus").isEqualTo("PAYED");
	}

	@Test
	@DisplayName("주문 실패 시 복구 로직을 진행한다")
	void orderRecovery() {
		// given
		Long productId = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();
		Long orderId = OrderHelper.createOrderAndGetId(productId, accessToken);
		Long beforeProductQuantity = ProductHelper.getProductDetail(productId).getQuantity();

		when(paymentClient.validatePayment(any(), any(), any())).thenReturn(
			Mono.error(IllegalArgumentException::new));

		//when
		ExtractableResponse<Response> response = OrderHelper.payOrder(
			OrderHelper.createOrderPayRequest(), orderId, accessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(400);

		Long afterProductQuantity = ProductHelper.getProductDetail(productId).getQuantity();
		assertThat(afterProductQuantity).isEqualTo(beforeProductQuantity);

		OrderDetailResponse orderResponse = OrderHelper.getOrder(orderId, accessToken);
		assertThat(orderResponse).extracting("orderStatus").isEqualTo("CANCELED");
	}

	@Test
	@DisplayName("회원이 주문 상세 정보를 조회한다.")
	void findById() {
		// given
		Long productId = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();
		Long orderId = OrderHelper.createOrderAndGetId(productId, accessToken);

		when(paymentClient.validatePayment(any(), any(), any())).thenReturn(Mono.empty());
		OrderHelper.payOrder(OrderHelper.createOrderPayRequest(), orderId, accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/orders/" + orderId, accessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		OrderDetailResponse body = response.as(OrderDetailResponse.class);
		assertThat(body).extracting(OrderDetailResponse::getOrderId).isEqualTo(orderId);
		assertThat(body).extracting(OrderDetailResponse::getOrderStatus).isEqualTo(OrderStatus.PAYED.name());
		assertThat(body).extracting(OrderDetailResponse::getTotalPrice).isEqualTo(3000L);
	}

	@Test
	@DisplayName("회원이 본인 주문 리스트를 조회한다.")
	void findAll() {
		// given
		Long productId = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();
		Long orderId1 = OrderHelper.createOrderAndGetId(productId, accessToken);
		Long orderId2 = OrderHelper.createOrderAndGetId(productId, accessToken);

		when(paymentClient.validatePayment(any(), any(), any())).thenReturn(Mono.empty());
		OrderHelper.payOrder(OrderHelper.createOrderPayRequest(), orderId1, accessToken);
		OrderHelper.payOrder(OrderHelper.createOrderPayRequest(), orderId2, accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/orders", accessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List body = response.as(List.class);
		assertThat(body).hasSize(2);
	}
}
