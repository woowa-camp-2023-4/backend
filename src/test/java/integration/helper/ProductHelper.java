package integration.helper;

import java.time.LocalDate;

import com.woowa.woowakit.shop.product.domain.product.ProductStatus;
import com.woowa.woowakit.shop.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.shop.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.shop.product.dto.request.StockCreateRequest;
import com.woowa.woowakit.shop.product.dto.response.ProductResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ProductHelper {

	public static String createProduct(final ProductCreateRequest request,
		final String adminAccessToken) {
		return CommonRestAssuredUtils.post("/products", request, adminAccessToken)
			.header("location");
	}

	public static ExtractableResponse<Response> createStockOfProduct(
		final String location,
		final StockCreateRequest request,
		final String adminAccessToken
	) {
		return CommonRestAssuredUtils.post(location + "/stocks", request, adminAccessToken);
	}

	public static ExtractableResponse<Response> updateProductStatus(
		final String location,
		final ProductStatusUpdateRequest request,
		final String adminAccessToken
	) {
		return CommonRestAssuredUtils.patch("/products/" + getIdFrom(location) + "/status", request,
			adminAccessToken);
	}

	public static ProductStatusUpdateRequest createProductStatusUpdateRequest(
		ProductStatus productStatus) {
		return ProductStatusUpdateRequest.of(productStatus);
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

	private static Long getIdFrom(String location) {
		String[] parts = location.split("/");
		return Long.parseLong(parts[parts.length - 1]);
	}

	public static Long createProductAndSetUp() {
		return createProductAndSetUp(10L);
	}

	public static Long createProductAndSetUp(final long quantity) {
		return createProductAndSetUp(quantity, ProductStatus.IN_STOCK);
	}

	public static Long createProductAndSetUp(final long quantity, final ProductStatus productStatus) {
		String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(),
			adminAccessToken);

		StockCreateRequest stockCreateRequest = ProductHelper.createStockCreateRequest(quantity);
		ProductHelper.createStockOfProduct(location, stockCreateRequest, adminAccessToken);

		ProductStatusUpdateRequest productStatusUpdateRequest = ProductHelper.createProductStatusUpdateRequest(
			productStatus);
		ProductHelper.updateProductStatus(location, productStatusUpdateRequest, adminAccessToken);
		return getIdFrom(location);
	}

	public static ProductResponse getProductDetail(Long productId) {
		return CommonRestAssuredUtils.get("/products/" + productId).as(ProductResponse.class);
	}
}
