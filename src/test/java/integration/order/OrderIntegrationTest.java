package integration.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.payment.domain.PaymentService;

import integration.IntegrationTest;
import integration.helper.MemberHelper;
import integration.helper.OrderHelper;
import integration.helper.ProductHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("주문 통합 테스트")
class OrderIntegrationTest extends IntegrationTest {

	@MockBean
	private PaymentService paymentService;

	@Test
	@DisplayName("가주문을 생성한다")
	void preOrder() {
		// given
		Long productId = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();

		// when
		ExtractableResponse<Response> response = OrderHelper.createPreOrder(
			OrderHelper.createPreOrderCreateRequest(productId), accessToken);

		// then
		PreOrderResponse preOrderResponse = response.as(PreOrderResponse.class);
		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(preOrderResponse).extracting(PreOrderResponse::getId,
				PreOrderResponse::getUuid)
			.isNotNull();
		assertThat(preOrderResponse).extracting(PreOrderResponse::getOrderItems).asList()
			.hasSize(1);
	}

	@Test
	@DisplayName("주문을 진행한다")
	void order() {
		// given
		Long productId = ProductHelper.createProductAndSetUp();
		String accessToken = MemberHelper.signUpAndLogIn();
		Long orderId = OrderHelper.createPreOrderAndGetId(productId, accessToken);
		Long beforeProductQuantity = ProductHelper.getProductDetail(productId).getQuantity();

		//when
		ExtractableResponse<Response> response = OrderHelper.createOrder(
			OrderHelper.createOrderCreateRequest(orderId), accessToken);

		// then
		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.as(Long.class)).isNotNull();
		Long afterProductQuantity = ProductHelper.getProductDetail(productId).getQuantity();
		assertThat(afterProductQuantity).isEqualTo(beforeProductQuantity - 1);
	}
}
