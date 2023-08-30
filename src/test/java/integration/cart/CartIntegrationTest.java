package integration.cart;

import com.woowa.woowakit.domain.cart.domain.CartItemSpecification;
import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domain.cart.dto.CartItemUpdateQuantityRequest;
import com.woowa.woowakit.domain.product.domain.product.ProductStatus;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("장바구니 인수테스트")
class CartIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("사용자는 상품을 가능한 수량만큼 장바구니에 담을 수 있다.")
    void addCartItem() {
        // given
        Long productId = ProductHelper.createProductAndSetUp();

        String accessToken = MemberHelper.signUpAndLogIn();

        CartItemAddRequest cartItemAddRequest = CartItemAddRequest.of(productId, 5L);

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
        Long productId = ProductHelper.createProductAndSetUp(100);

        String accessToken = MemberHelper.signUpAndLogIn();

        CartItemHelper.createCartItemAddRequest(productId, 10);

        // when
        ExtractableResponse<Response> response = CartItemHelper.addCartItem(productId, 2, accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).matches("^/cart-items/[0-9]+$");
    }

    @Test
    @DisplayName("사용자는 상품 재고보다 많은 수량을 장바구니에 추가하고자 하는 경우 에러를 던진다.")
    void addCartItemFail() {
        // given
        Long productId = ProductHelper.createProductAndSetUp(100);

        String accessToken = MemberHelper.signUpAndLogIn();

        // when
        ExtractableResponse<Response> response = CartItemHelper.addCartItem(productId, 102, accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.");
    }

    @Test
    @DisplayName("이미 장바구니에 있는 상품 수량 ,추가하고자하는 수량을 더한 경우가 상품의 재고보다 큰 경우 에러를 던진다.")
    void addCartItemPlusFail() {
        // given
        Long productId = ProductHelper.createProductAndSetUp(100);

        String accessToken = MemberHelper.signUpAndLogIn();

        CartItemHelper.addCartItem(productId, 10, accessToken);

        // when
        ExtractableResponse<Response> response = CartItemHelper.addCartItem(productId, 91, accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.");
    }

    @Test
    @DisplayName("상품의 상태가 IN_STOCK 가 아닌 경우 장바구니에 상품을 담을 수 없다.")
    void addCartItemStatusFail() {
        // given
        Long productId = ProductHelper.createProductAndSetUp(100, ProductStatus.STOPPED);

        String accessToken = MemberHelper.signUpAndLogIn();

        // when
        ExtractableResponse<Response> response = CartItemHelper.addCartItem(productId, 91, accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("상품을 구매할 수 없는 상태입니다.");
    }

    @Test
    @DisplayName("사용자는 장바구니에 추가한 아이템을 조회할 수 있다.")
    void readCartItem() {
        // given
        Long productId = ProductHelper.createProductAndSetUp(10);
        Long otherProductId = ProductHelper.createProductAndSetUp(50);

        String accessToken = MemberHelper.signUpAndLogIn();

        CartItemHelper.addCartItem(productId, 5, accessToken);

        CartItemHelper.addCartItem(otherProductId, 10, accessToken);

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

    @Test
    @DisplayName("사용자는 장바구니 상품의 수량을 조절할 수 있다.")
    void updateQuantity() {
        // given
        Long productId = ProductHelper.createProductAndSetUp(50);

        String accessToken = MemberHelper.signUpAndLogIn();

        Long cartItemId = CartItemHelper.addCartItemAndGetID(productId, 5, accessToken);

        // when
        CommonRestAssuredUtils.patch(
                "/cart-items/" + cartItemId + "/quantity", CartItemUpdateQuantityRequest.from(30L), accessToken);

        // then
        ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/cart-items", accessToken);
        List<CartItemSpecification> cartItemResponses = response.as(ArrayList.class);
        assertThat(cartItemResponses)
                .hasSize(1)
                .extracting("quantity")
                .contains(30);
    }

    @Test
    @DisplayName("사용자는 장바구니 상품을 삭제할 수 있다.")
    void deleteCartItem() {
        // given
        Long productId = ProductHelper.createProductAndSetUp(10);

        String accessToken = MemberHelper.signUpAndLogIn();

        Long cartItemId = CartItemHelper.addCartItemAndGetID(productId, 5, accessToken);

        // when
        CommonRestAssuredUtils.delete("/cart-items/" + cartItemId, accessToken);

        // then
        ExtractableResponse<Response> response = CommonRestAssuredUtils.get("/cart-items", accessToken);
        List<CartItemSpecification> cartItemResponses = response.as(ArrayList.class);
        assertThat(cartItemResponses)
                .isEmpty();
    }
}
