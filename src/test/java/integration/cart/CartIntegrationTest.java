package integration.cart;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.woowa.woowakit.domain.cart.domain.CartItemSpecification;
import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domain.product.domain.product.ProductStatus;
import com.woowa.woowakit.domain.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;

import integration.IntegrationTest;
import integration.helper.CartItemHelper;
import integration.helper.CommonRestAssuredUtils;
import integration.helper.MemberHelper;
import integration.helper.ProductHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("장바구니 인수테스트")
class CartIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("사용자는 상품을 가능한 수량만큼 장바구니에 담을 수 있다.")
	void addCartItem() {
		// given
		String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());

		String location = createProductAndCreateStockAndUpdateProductStatus(adminAccessToken, 10,
			ProductStatus.IN_STOCK);

		String accessToken = signUpMemberAndLogin();

		CartItemAddRequest cartItemAddRequest = CartItemAddRequest.of(getIdFrom(location), 5L);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/cart-items", cartItemAddRequest,
			accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).matches("^/cart-items/[0-9]+$");
	}

	@Test
	@DisplayName("사용자는 이미 장바구니에 추가한 상품을 또 장바구니에 담을 수 있다.")
	void addCartItemPlus() {
		// given
		String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = createProductAndCreateStockAndUpdateProductStatus(adminAccessToken, 10,
			ProductStatus.IN_STOCK);

		String accessToken = signUpMemberAndLogin();

		addCartItem(location, 5, accessToken);

		CartItemAddRequest nextCartItemAddRequest = CartItemHelper.createCartItemAddRequest(getIdFrom(location), 2);

		// when
		ExtractableResponse<Response> response = CartItemHelper.addCartItem(nextCartItemAddRequest, accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).matches("^/cart-items/[0-9]+$");
	}

	@Test
	@DisplayName("사용자는 상품 재고보다 많은 수량을 장바구니에 추가하고자 하는 경우 에러를 던진다.")
	void addCartItemFail() {
		// given
		String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = createProductAndCreateStockAndUpdateProductStatus(adminAccessToken, 4,
			ProductStatus.IN_STOCK);

		String accessToken = signUpMemberAndLogin();

		CartItemAddRequest cartItemAddRequest = CartItemAddRequest.of(getIdFrom(location), 5L);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/cart-items", cartItemAddRequest,
			accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("message")).isEqualTo("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.");
	}

	@Test
	@DisplayName("이미 장바구니에 있는 상품 수량 ,추가하고자하는 수량을 더한 경우가 상품의 재고보다 큰 경우 에러를 던진다.")
	void addCartItemPlusFail() {
		// given
		String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = createProductAndCreateStockAndUpdateProductStatus(adminAccessToken, 9,
			ProductStatus.IN_STOCK);

		String accessToken = signUpMemberAndLogin();

		addCartItem(location, 5, accessToken);

		CartItemAddRequest nextCartItemAddRequest = CartItemHelper.createCartItemAddRequest(getIdFrom(location), 10);

		// when
		ExtractableResponse<Response> response = CartItemHelper.addCartItem(nextCartItemAddRequest, accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("message")).isEqualTo("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.");
	}

	@Test
	@DisplayName("상품의 상태가 IN_STOCK 가 아닌 경우 장바구니에 상품을 담을 수 없다.")
	void addCartItemStatusFail() {
		// given
		String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = createProductAndCreateStockAndUpdateProductStatus(adminAccessToken, 9,
			ProductStatus.SOLD_OUT);

		String accessToken = signUpMemberAndLogin();

		CartItemAddRequest cartItemAddRequest = CartItemHelper.createCartItemAddRequest(getIdFrom(location), 5);

		// when
		ExtractableResponse<Response> response = CartItemHelper.addCartItem(cartItemAddRequest, accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.jsonPath().getString("message")).isEqualTo("상품을 구매할 수 없는 상태입니다.");
	}

	@Test
	@DisplayName("사용자는 장바구니에 추가한 아이템을 조회할 수 있다.")
	void readCartItem() {
		// given
		String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
		String location = createProductAndCreateStockAndUpdateProductStatus(adminAccessToken, 10,
			ProductStatus.IN_STOCK);

		String otherLocation = createProductAndCreateStockAndUpdateProductStatus(adminAccessToken, 50,
			ProductStatus.IN_STOCK);

		String accessToken = signUpMemberAndLogin();

		addCartItem(location, 5, accessToken);
		addCartItem(otherLocation, 10, accessToken);

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/cart-items", accessToken);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List<CartItemSpecification> cartItemResponses = response.as(ArrayList.class);
		assertThat(cartItemResponses)
			.hasSize(2)
			.extracting("quantity")
			.contains(5, 10);
	}

	private void addCartItem(String location, int quantity, String accessToken) {
		CartItemAddRequest cartItemAddRequest = CartItemHelper.createCartItemAddRequest(getIdFrom(location), quantity);
		CartItemHelper.addCartItem(cartItemAddRequest, accessToken);
	}

	private String signUpMemberAndLogin() {
		MemberHelper.signup(MemberHelper.createSignUpRequest());
		return MemberHelper.login(MemberHelper.createLoginRequest());
	}

	private String createProductAndCreateStockAndUpdateProductStatus(String adminAccessToken, int quantity,
		ProductStatus inStock) {
		String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), adminAccessToken);
		StockCreateRequest stockCreateRequest = ProductHelper.createStockCreateRequest(quantity);
		ProductHelper.createStockOfProduct(location, stockCreateRequest, adminAccessToken);
		ProductStatusUpdateRequest productStatusUpdateRequest = ProductHelper.createProductStatusUpdateRequest(inStock);
		ProductHelper.updateProductStatus(location, productStatusUpdateRequest, adminAccessToken);
		return location;
	}

	private Long getIdFrom(String location) {
		String[] parts = location.split("/");
		return Long.parseLong(parts[parts.length - 1]);
	}
}
