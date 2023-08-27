package com.woowa.woowakit.domain.cart.api;

import com.woowa.woowakit.domain.cart.application.CartItemService;
import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemSpecification;
import com.woowa.woowakit.domain.cart.dto.CartItemAddRequest;
import com.woowa.woowakit.domain.cart.dto.CartItemUpdateQuantityRequest;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.ProductImage;
import com.woowa.woowakit.domain.product.domain.product.ProductName;
import com.woowa.woowakit.domain.product.domain.product.ProductPrice;
import com.woowa.woowakit.restDocsHelper.PathParam;
import com.woowa.woowakit.restDocsHelper.RequestFields;
import com.woowa.woowakit.restDocsHelper.ResponseFields;
import com.woowa.woowakit.restDocsHelper.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static com.woowa.woowakit.restDocsHelper.RestDocsHelper.authorizationDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemController.class)
@AutoConfigureRestDocs(uriHost = "api.test.com", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
class CartItemControllerTest extends RestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;

    @Test
    @DisplayName("[GET] [/cart-items] 장바구니 조회 테스트 및 문서화")
    void readCartItemsTest() throws Exception {
        ResponseFields responseFields = new ResponseFields(Map.of(
                "[]quantity", "장바구니 아이템 수량",
                "[]productPrice", "상품 가격",
                "[]productName", "상품 이름",
                "[]productImage", "상품 이미지 URL",
                "[]productId", "상품 ID",
                "[]cartItemId", "장바구니 아이템 ID"
        ));

        String token = getToken();
        List<CartItemSpecification> responses = List.of(
                getCartItemSpecification(2, 15000, "된장 밀키트", 1L, 2L),
                getCartItemSpecification(5, 30000, "닯갈비 밀키트", 2L, 4L)
        );
        given(cartItemService.readCartItem(any())).willReturn(responses);

        mockMvc.perform(get("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("readCartItems"))
                .andDo(authorizationDocument("cartItem/get", responseFields));
    }

    @Test
    @DisplayName("[POST] [/cart-items] 장바구니 추가 테스트 및 문서화")
    void addCartItem() throws Exception {
        RequestFields requestFields = new RequestFields(Map.of(
                "productId", "상품 ID",
                "quantity", "상품 수량"
        ));

        String token = getToken();
        CartItemAddRequest request = CartItemAddRequest.of(1L, 10L);
        given(cartItemService.addCartItem(any(), any())).willReturn(CartItem.of(1L, 2L));

        mockMvc.perform(post("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("addCartItem"))
                .andDo(authorizationDocument("cartItem/post", requestFields));
    }

    @Test
    @DisplayName("[DELETE] [/cart-items] 장바구니 삭제 테스트 및 문서화")
    void deleteCartItem() throws Exception {
        PathParam pathParam = new PathParam("id", "장바구니 아이템");
        String token = getToken();

        mockMvc.perform(delete("/cart-items/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteCartItem"))
                .andDo(authorizationDocument("cartItem/delete", pathParam));
    }

    @Test
    @DisplayName("[Patch] [/cart-items] 장바구니 수정 테스트 및 문서화")
    void updateQuantity() throws Exception {
        PathParam pathParam = new PathParam("id", "장바구니 아이템");
        RequestFields requestFields = new RequestFields(Map.of(
                "quantity", "변경할 상품 수량"
        ));

        String token = getToken();
        CartItemUpdateQuantityRequest request = CartItemUpdateQuantityRequest.from(10);

        mockMvc.perform(patch("/cart-items/{id}/quantity", 1L)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateQuantity"))
                .andDo(authorizationDocument("cartItem/patch", pathParam, requestFields));
    }

    private CartItemSpecification getCartItemSpecification(
            final int quantity,
            final long price,
            final String name,
            final long productId,
            final long cartItemId
    ) {
        return new CartItemSpecification(
                Quantity.from(quantity),
                ProductPrice.from(price),
                ProductName.from(name),
                ProductImage.from("https://service-hub.org/file/logo/8e18bb9c-3a96-48a5-979e-e0f16ba8cde2.png"),
                productId,
                cartItemId
        );
    }
}
