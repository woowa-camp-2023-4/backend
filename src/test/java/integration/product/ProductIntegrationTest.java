package integration.product;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.woowa.woowakit.domain.product.domain.product.ProductStatus;
import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductResponse;

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
		String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get(location);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		ProductResponse detailResponse = response.as(ProductResponse.class);
		assertThat(detailResponse).extracting("quantity").isEqualTo(0L);
		assertThat(detailResponse).extracting("imageUrl").isEqualTo("testImage");
		assertThat(detailResponse).extracting("status").isEqualTo("PRE_REGISTRATION");
	}

	@Test
	@DisplayName("일일 판매량 순서를 바탕으로 메인 페이지를 조회할 수 있다.")
	void mainPage() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest2(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest2(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest3(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest3(), accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/products/rank");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List<ProductResponse> responses = response.as(ArrayList.class);
		assertThat(responses).hasSize(6);
	}

	@Test
	@DisplayName("상품 리스트를 검색 조건에 따라 cursor 방식으로 조회할 수 있다.")
	void findAll() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest2(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest2(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest3(), accessToken);
		ProductHelper.createProduct(ProductHelper.createProductCreateRequest3(), accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/products",
			Map.of("pageSize", 5, "productKeyword", "2"));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List<ProductResponse> responses = response.as(ArrayList.class);
		assertThat(responses).hasSize(2);
	}

	@Test
	@DisplayName("유통기한, 수량을 입력받아 재고를 추가할 수 있다.")
	void addStock() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);

		// when
		StockCreateRequest request = ProductHelper.createStockCreateRequest(5L);
		ExtractableResponse<Response> response = ProductHelper.createStockOfProduct(location, request, accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).matches("^/products/[0-9]+/stocks/[0-9]+$");

		ProductResponse productResponse = CommonRestAssuredUtils.get(location).as(ProductResponse.class);
		assertThat(productResponse).extracting("quantity").isEqualTo(5L);
	}

	@Test
	@DisplayName("가등록 상태인 상품을 판매 중 상태를 나타내는 IN_STOCK 상태로 변경할 수 있다.")
	void updateStatusToIn_STOCK() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);

		StockCreateRequest request = ProductHelper.createStockCreateRequest(5L);
		ProductHelper.createStockOfProduct(location, request, accessToken);

		ProductStatusUpdateRequest productStatusUpdateRequest = ProductHelper.createProductStatusUpdateRequest(
			ProductStatus.IN_STOCK);

		// when
		ExtractableResponse<Response> response = ProductHelper.updateProductStatus(location, productStatusUpdateRequest,
			accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		ProductResponse productResponse = CommonRestAssuredUtils.get(location).as(ProductResponse.class);
		assertThat(productResponse).extracting("status").isEqualTo("IN_STOCK");
	}

	@Test
	@DisplayName("판매 중인 상품을 STOPPED 상태로 변경할 수 있다.")
	void updateStatusTo_STOPPED() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);

		StockCreateRequest request = ProductHelper.createStockCreateRequest(5L);
		ProductHelper.createStockOfProduct(location, request, accessToken);

		ProductStatusUpdateRequest productStatusUpdateRequest = ProductHelper.createProductStatusUpdateRequest(
			ProductStatus.IN_STOCK);
		ProductHelper.updateProductStatus(location, productStatusUpdateRequest, accessToken);

		ProductStatusUpdateRequest nextProductStatusUpdateRequest = ProductHelper.createProductStatusUpdateRequest(
			ProductStatus.STOPPED);

		// when
		ExtractableResponse<Response> response = ProductHelper.updateProductStatus(location,
			nextProductStatusUpdateRequest, accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		ProductResponse productResponse = CommonRestAssuredUtils.get(location).as(ProductResponse.class);
		assertThat(productResponse).extracting("status").isEqualTo("STOPPED");
	}

	@Test
	@DisplayName("재고가 0인 상품을 IN_STOCK 상태로 변경할 수 없다.")
	void updateStatusFail() {
		// given
		String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), accessToken);

		StockCreateRequest request = ProductHelper.createStockCreateRequest(0L);
		ProductHelper.createStockOfProduct(location, request, accessToken);

		ProductStatusUpdateRequest productStatusUpdateRequest = ProductStatusUpdateRequest.of(ProductStatus.IN_STOCK);

		// when
		ExtractableResponse<Response> response = ProductHelper.updateProductStatus(location, productStatusUpdateRequest,
			accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("message")).isEqualTo("재고가 0인 상태는 판매 중 상태로 변경할 수 없습니다.");
	}
}
