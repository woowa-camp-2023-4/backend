package integration.helper;

import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CartItemHelper {

	public static ExtractableResponse<Response> addCartItem(
		final CartItemAddRequest request,
		final String accessToken
	) {
		return CommonRestAssuredUtils.post("/cart-items", request, accessToken);
	}

	public static CartItemAddRequest createCartItemAddRequest(long productId, long quantity) {
		return CartItemAddRequest.of(productId, quantity);
	}
}
