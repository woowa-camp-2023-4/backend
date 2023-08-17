package integration.payment;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.woowa.woowakit.domain.payment.dto.request.PaymentSuccessRequest;

import integration.IntegrationTest;
import integration.helper.CommonRestAssuredUtils;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("결제 인수 테스트")
public class PaymentIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("결제 성공한다.")
	void paymentSuccess() {
		// given
		PaymentSuccessRequest request = PaymentSuccessRequest.of(1L, "order-1", 50000L, "paymentKey");

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/payments/success", request);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
