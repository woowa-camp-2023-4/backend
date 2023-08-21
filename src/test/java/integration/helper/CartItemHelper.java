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

	public static CartItemAddRequest createCartItemAddRequest(long productId, long quantity) {
		return CartItemAddRequest.of(productId, quantity);
	}
}
