package integration.cart;

import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;
import integration.IntegrationTest;
import integration.helper.CartItemHelper;
import integration.helper.CommonRestAssuredUtils;
import integration.helper.MemberHelper;
import integration.helper.ProductHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("장바구니 인수테스트")
class CartIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("사용자는 상품을 가능한 수량만큼 장바구니에 담을 수 있다.")
    void addCartItem() {
        // given
        String adminAccessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
        String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), adminAccessToken);

        StockCreateRequest stockCreateRequest = ProductHelper.createStockCreateRequest(10);
        ProductHelper.createStockOfProduct(location, stockCreateRequest, adminAccessToken);

        MemberHelper.signup(MemberHelper.createSignUpRequest());
        String accessToken = MemberHelper.login(MemberHelper.createLoginRequest());

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
        String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), adminAccessToken);

        StockCreateRequest stockCreateRequest = ProductHelper.createStockCreateRequest(10);
        ProductHelper.createStockOfProduct(location, stockCreateRequest, adminAccessToken);

        MemberHelper.signup(MemberHelper.createSignUpRequest());
        String accessToken = MemberHelper.login(MemberHelper.createLoginRequest());

        CartItemAddRequest cartItemAddRequest = CartItemHelper.createCartItemAddRequest(getIdFrom(location), 5);
        CartItemHelper.addCartItem(cartItemAddRequest, accessToken);

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
        String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), adminAccessToken);

        StockCreateRequest stockCreateRequest = ProductHelper.createStockCreateRequest(4);
        ProductHelper.createStockOfProduct(location, stockCreateRequest, adminAccessToken);

        MemberHelper.signup(MemberHelper.createSignUpRequest());
        String accessToken = MemberHelper.login(MemberHelper.createLoginRequest());

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
        String location = ProductHelper.createProduct(ProductHelper.createProductCreateRequest(), adminAccessToken);

        StockCreateRequest stockCreateRequest = ProductHelper.createStockCreateRequest(9);
        ProductHelper.createStockOfProduct(location, stockCreateRequest, adminAccessToken);

        MemberHelper.signup(MemberHelper.createSignUpRequest());
        String accessToken = MemberHelper.login(MemberHelper.createLoginRequest());

        CartItemAddRequest cartItemAddRequest = CartItemHelper.createCartItemAddRequest(getIdFrom(location), 5);
        CartItemHelper.addCartItem(cartItemAddRequest, accessToken);

        CartItemAddRequest nextCartItemAddRequest = CartItemHelper.createCartItemAddRequest(getIdFrom(location), 10);

        // when
        ExtractableResponse<Response> response = CartItemHelper.addCartItem(nextCartItemAddRequest, accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.");
    }

    private Long getIdFrom(String location) {
        String[] parts = location.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
