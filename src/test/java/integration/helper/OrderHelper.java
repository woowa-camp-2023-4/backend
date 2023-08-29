package integration.helper;

import java.util.List;

import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateCartItemRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class OrderHelper {

	public static ExtractableResponse<Response> createPreOrder(
		final PreOrderCreateRequest request, final String accessToken
	) {
		return CommonRestAssuredUtils.post("/orders/pre", request, accessToken);
	}

	public static ExtractableResponse<Response> createPreOrder(
		final PreOrderCreateCartItemRequest request, final String accessToken
	) {
		return CommonRestAssuredUtils.post("/orders/pre-cart-item", List.of(request), accessToken);
	}

	public static PreOrderCreateRequest createPreOrderCreateRequest(Long productId) {
		return PreOrderCreateRequest.of(productId, 1L);
	}

	public static PreOrderCreateCartItemRequest createPreOrderCreateCartItemRequest(Long cartItemId) {
		return new PreOrderCreateCartItemRequest(cartItemId);
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

	public static OrderCreateRequest createOrderCreateRequest(final Long preOrderId) {
		return OrderCreateRequest.of(preOrderId, "paymentKey");
	}

	public static OrderDetailResponse getOrder(
		final Long orderId,
		final String accessToken
	) {
		return CommonRestAssuredUtils.get("/orders/" + orderId, accessToken).as(OrderDetailResponse.class);
	}
}
