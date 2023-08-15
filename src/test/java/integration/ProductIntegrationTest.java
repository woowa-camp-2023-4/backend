package integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("Product 인수 테스트")
public class ProductIntegrationTest extends IntegrationTest {

	// todo: Helper 클래스 패키지, Test Fixture 패키지 어디에 둘 지 토론
	public static <T> ExtractableResponse<Response> post(String url, T body) {
		return RestAssured.given().log().all()
			.body(body)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post(url)
			.then().log().all()
			.extract();
	}

	@Test
	@DisplayName("상품 이름, 가격, 이미지 주소를 입력해 Product를 생성할 수 있다.")
	void createWithName() {
		// given
		ProductCreateRequest request = ProductCreateRequest.of("test", 3000L, "testImage");

		// when
		ExtractableResponse<Response> response = post("/product", request);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isEqualTo("/products/1");
	}
}
