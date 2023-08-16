package integration.product;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;

import integration.IntegrationTest;
import integration.helper.CommonRestAssuredUtils;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("Product 인수 테스트")
public class ProductIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("상품 이름, 가격, 이미지 주소를 입력해 Product를 생성할 수 있다.")
	void createWithName() {
		// given
		ProductCreateRequest request = ProductCreateRequest.of("test", 3000L, "testImage");

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/products", request);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).matches("^/products/[0-9]+$");
	}

	@Test
	@DisplayName("상품 Id로 상품 상세 정보를 조회할 수 있다.")
	void findById() {
		// given
		ProductCreateRequest request = ProductCreateRequest.of("test", 3000L, "testImage");
		String location = CommonRestAssuredUtils.post("/products", request).header("Location");

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get(location);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		ProductDetailResponse detailResponse = response.as(ProductDetailResponse.class);
		assertThat(detailResponse).extracting("quantity").isEqualTo(0L);
		assertThat(detailResponse).extracting("imageUrl").isEqualTo("http://localhost:8080/testImage");
		assertThat(detailResponse).extracting("status").isEqualTo("PRE_REGISTRATION");
	}
}
