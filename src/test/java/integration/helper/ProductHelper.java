package integration.helper;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;

public class ProductHelper {

	public static String createProduct(final ProductCreateRequest request, final String accessToken) {
		return CommonRestAssuredUtils.post("/products", request, accessToken).header("location");
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
}
