package integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;

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

	public static <T> ExtractableResponse<Response> get(String url) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get(url)
			.then().log().all()
			.extract();
	}

	@Test
	@DisplayName("상품 이름, 가격, 이미지 주소를 입력해 Product를 생성할 수 있다.")
	void createWithName() {
		// given
		ProductCreateRequest request = ProductCreateRequest.of("test", 3000L, "testImage");

		// when
		ExtractableResponse<Response> response = post("/products", request);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).matches("^/products/[0-9]+$");
	}

	@Test
	@DisplayName("상품 Id로 상품 상세 정보를 조회할 수 있다.")
	void findById() {
		// given
		ProductCreateRequest request = ProductCreateRequest.of("test", 3000L, "testImage");
		String location = post("/products", request).header("Location");

		// when
		ExtractableResponse<Response> response = get(location);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		ProductDetailResponse detailResponse = response.as(ProductDetailResponse.class);
		assertThat(detailResponse).extracting("quantity").isEqualTo(0L);
		assertThat(detailResponse).extracting("imageUrl").isEqualTo("http://localhost:8080/testImage");
		assertThat(detailResponse).extracting("status").isEqualTo("PRE_REGISTRATION");
	}
}
