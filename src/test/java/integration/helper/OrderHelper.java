package integration.helper;

import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class OrderHelper {

    public static ExtractableResponse<Response> createPreOrder(
        final PreOrderCreateRequest request, final String accessToken) {
        return CommonRestAssuredUtils.post("/orders/pre", request, accessToken);
    }

    public static PreOrderCreateRequest createPreOrderCreateRequest(Long productId) {
        return PreOrderCreateRequest.of(productId, 1L);
    }

    public static Long createPreOrderAndGetId(Long productId, String accessToken) {
        return OrderHelper.createPreOrder(
                OrderHelper.createPreOrderCreateRequest(productId), accessToken)
            .as(PreOrderResponse.class).getId();
    }

    public static ExtractableResponse<Response> createOrder(
        final OrderCreateRequest request,
        final String accessToken
    ) {
        return CommonRestAssuredUtils.post("/orders", request, accessToken);
    }

    public static OrderCreateRequest createOrderCreateRequest(Long preOrderId) {
        return OrderCreateRequest.of(preOrderId, "paymentKey");
    }
}
