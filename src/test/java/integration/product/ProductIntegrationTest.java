package integration.product;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;

import integration.IntegrationTest;
import integration.helper.CommonRestAssuredUtils;
import integration.helper.MemberHelper;
import integration.helper.ProductHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("Product 인수 테스트")
class ProductIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("관리자는 상품 이름, 가격, 이미지 주소를 입력해 Product를 생성할 수 있다.")
	void createWithName() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		ProductCreateRequest request = ProductHelper.createProductCreateRequest();

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/products", request, accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).matches("^/products/[0-9]+$");
	}

	@Test
	@DisplayName("상품 Id로 상품 상세 정보를 조회할 수 있다.")
	void findById() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		ProductCreateRequest request = ProductCreateRequest.of("test", 3000L, "testImage");
		String location = ProductHelper.createProduct(request, accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get(location);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		ProductDetailResponse detailResponse = response.as(ProductDetailResponse.class);
		assertThat(detailResponse).extracting("quantity").isEqualTo(0L);
		assertThat(detailResponse).extracting("imageUrl").isEqualTo("http://localhost:8080/testImage");
		assertThat(detailResponse).extracting("status").isEqualTo("PRE_REGISTRATION");
	}

	@Test
	@DisplayName("상품 리스트를 조회할 수 있다.")
	void findAll() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest2(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest3(), accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/products");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List<ProductDetailResponse> responses = response.as(ArrayList.class);
		assertThat(responses).hasSize(3);
	}
}
