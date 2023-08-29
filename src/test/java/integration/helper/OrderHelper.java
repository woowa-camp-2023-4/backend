package integration.helper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.OrderPayRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domain.order.dto.response.OrderResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class OrderHelper {

	public static ExtractableResponse<Response> createOrder(
		final List<OrderCreateRequest> request, final String accessToken
	) {
		return CommonRestAssuredUtils.post("/orders", request, accessToken);
	}

	public static List<OrderCreateRequest> createOrderRequests(Long... ids) {
		return Arrays.stream(ids)
			.map(id -> OrderCreateRequest.of(id, 1L))
			.collect(Collectors.toList());
	}

	public static List<OrderCreateRequest> createOrderRequest(Long productId) {
		return List.of(OrderCreateRequest.of(productId, 1L));
	}

	public static Long createOrderAndGetId(Long productId, String accessToken) {
		return OrderHelper.createOrder(
				OrderHelper.createOrderRequest(productId), accessToken)
			.as(OrderResponse.class).getId();
	}

	public static ExtractableResponse<Response> payOrder(
		final OrderPayRequest request,
		final Long orderId,
		final String accessToken
	) {
		return CommonRestAssuredUtils.post("/orders/" + orderId + "/pay", request, accessToken);
	}

	public static OrderPayRequest createOrderPayRequest() {
		return OrderPayRequest.of("paymentKey");
	}

	public static OrderDetailResponse getOrder(
		final Long orderId,
		final String accessToken
	) {
		return CommonRestAssuredUtils.get("/orders/" + orderId, accessToken).as(OrderDetailResponse.class);
	}
}
