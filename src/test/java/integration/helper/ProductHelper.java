package integration.helper;

import java.time.LocalDate;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ProductHelper {

	public static String createProduct(final ProductCreateRequest request, final String adminAccessToken) {
		return CommonRestAssuredUtils.post("/products", request, adminAccessToken).header("location");
	}

	public static ExtractableResponse<Response> createStockOfProduct(
		final String location,
		final StockCreateRequest request,
		final String adminAccessToken
	) {
		return CommonRestAssuredUtils.post(location + "/stocks", request, adminAccessToken);
	}

	public static ProductCreateRequest createProductCreateRequest() {
		return ProductCreateRequest.of("test1", 3000L, "testImage");
	}

	public static ProductCreateRequest createProductCreateRequest2() {
		return ProductCreateRequest.of("test2", 7000L, "testImage");

	}

	public static ProductCreateRequest createProductCreateRequest3() {
		return ProductCreateRequest.of("test3", 10000L, "testImage");
	}

	public static StockCreateRequest createStockCreateRequest(long quantity) {
		return StockCreateRequest.of(LocalDate.now().plusDays(1), quantity);
	}
}
