package integration.helper;

import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CartItemHelper {

	public static ExtractableResponse<Response> addCartItem(
		final long productId,
		final long quantity,
		final String accessToken
	) {
		return CommonRestAssuredUtils.post("/cart-items", createCartItemAddRequest(productId, quantity), accessToken);
	}

	public static Long addCartItemAndGetID(
		final long productId,
		final long quantity,
		final String accessToken
	) {
		String location = CommonRestAssuredUtils.post("/cart-items", createCartItemAddRequest(productId, quantity),
			accessToken).header("Location");
		return getIdFrom(location);
	}

	public static CartItemAddRequest createCartItemAddRequest(long productId, long quantity) {
		return CartItemAddRequest.of(productId, quantity);
	}

	private static Long getIdFrom(String location) {
		String[] parts = location.split("/");
		return Long.parseLong(parts[parts.length - 1]);
	}
}
