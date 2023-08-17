package integration.product;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;
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

	@Test
	@DisplayName("상품 리스트를 조회할 수 있다.")
	void findAll() {
		// given
		ProductCreateRequest request1 = ProductCreateRequest.of("test1", 3000L, "testImage");
		CommonRestAssuredUtils.post("/products", request1).header("Location");

		ProductCreateRequest request2 = ProductCreateRequest.of("test2", 6000L, "testImage");
		CommonRestAssuredUtils.post("/products", request2).header("Location");

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/products");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List<ProductDetailResponse> responses = response.as(ArrayList.class);
		assertThat(responses).hasSize(2);
	}

	@Test
	@DisplayName("유통기한, 수량을 입력받아 재고를 추가할 수 있다.")
	void addStock() {
		// given
		ProductCreateRequest productRequest = ProductCreateRequest.of("test", 3000L, "testImage");
		String location = CommonRestAssuredUtils.post("/products", productRequest).header("Location");

		// when
		StockCreateRequest stockRequest = StockCreateRequest.of(LocalDate.now().plusDays(1), 5L);
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post(location + "/stocks", stockRequest);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).matches("^/products/[0-9]+/stocks/[0-9]+$");

		ProductDetailResponse productResponse = CommonRestAssuredUtils.get(location).as(ProductDetailResponse.class);
		assertThat(productResponse).extracting("quantity").isEqualTo(5L);
	}
}
