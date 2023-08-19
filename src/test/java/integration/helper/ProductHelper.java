package integration.helper;

import com.woowa.woowakit.domain.product.domain.product.ProductStatus;
import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.time.LocalDate;

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

    public static ExtractableResponse<Response> updateProductStatus(
            final String location,
            final ProductStatusUpdateRequest request,
            final String adminAccessToken
    ) {

        return CommonRestAssuredUtils.patch("/products/" + getIdFrom(location) + "/status", request, adminAccessToken);
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

    public static ProductStatusUpdateRequest createProductStatusUpdateRequest(ProductStatus productStatus) {
        return ProductStatusUpdateRequest.of(productStatus);
    }

    private static Long getIdFrom(String location) {
        String[] parts = location.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
