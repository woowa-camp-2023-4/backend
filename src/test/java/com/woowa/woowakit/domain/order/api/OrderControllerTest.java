package com.woowa.woowakit.domain.order.api;

import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.application.OrderService;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateCartItemRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureRestDocs(uriHost = "api.test.com", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
class OrderControllerTest extends RestDocsTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Test
	@DisplayName("[POST] [/orders/pre] 주문 생성 테스트 및 문서화")
	void createPreOrder() throws Exception {
		RequestFields requestFields = new RequestFields(Map.of(
			"productId", "상품 ID",
			"quantity", "상품 수량"
		));
		ResponseFields responseFields = new ResponseFields(Map.of(
			"id", "주문 아이디",
			"uuid", "주문 고유 번호",
			"orderItems[].id", "주문 상품 ID",
			"orderItems[].productId", "주문 상품 ID",
			"orderItems[].name", "주문 상품 이름",
			"orderItems[].image", "주문 상품 이미지 URL",
			"orderItems[].price", "주문 상품 가격",
			"orderItems[].quantity", "주문 상품 수량"
		));

		String token = getToken();
		PreOrderCreateRequest request = PreOrderCreateRequest.of(1L, 10L);
		PreOrderResponse response = PreOrderResponse.from(getOrder());
		given(orderService.preOrder(any(), any())).willReturn(response);

		mockMvc.perform(post("/orders/pre")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(handler().methodName("createPreOrder"))
			.andDo(authorizationDocument("orders/pre", requestFields, responseFields));
	}

	@Test
	@DisplayName("[POST] [/orders/pre-cart-item] 장바구니 주문 생성 테스트 및 문서화")
	void createPreOrderByCartItems() throws Exception {
		RequestFields requestFields = new RequestFields(Map.of(
			"[]cartItemId", "장바구니 ID"
		));
		ResponseFields responseFields = new ResponseFields(Map.of(
			"id", "주문 아이디",
			"uuid", "주문 고유 번호",
			"orderItems[].id", "주문 상품 ID",
			"orderItems[].productId", "주문 상품 ID",
			"orderItems[].name", "주문 상품 이름",
			"orderItems[].image", "주문 상품 이미지 URL",
			"orderItems[].price", "주문 상품 가격",
			"orderItems[].quantity", "주문 상품 수량"
		));

		String token = getToken();
		List<PreOrderCreateCartItemRequest> requests = List.of(
			PreOrderCreateCartItemRequest.from(1L),
			PreOrderCreateCartItemRequest.from(3L));
		PreOrderResponse response = PreOrderResponse.from(getOrder());
		given(orderService.preOrderCartItems(any(), any())).willReturn(response);

		mockMvc.perform(post("/orders/pre-cart-item")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requests)))
			.andExpect(status().isCreated())
			.andExpect(handler().methodName("createPreOrderByCartItems"))
			.andDo(authorizationDocument("orders/pre-cart", requestFields, responseFields));
	}

	@Test
	@DisplayName("[POST] [/orders/order] 주문 테스트 및 문서화")
	void createOrder() throws Exception {
		RequestFields requestFields = new RequestFields(Map.of(
			"orderId", "주문 ID",
			"paymentKey", "결제 키"
		));

		String token = getToken();
		OrderCreateRequest request = OrderCreateRequest.of(1L, "paymentKey");
		Long response = 1L;
		given(orderService.order(any(), any())).willReturn(response);

		mockMvc.perform(post("/orders")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("createOrder"))
			.andDo(authorizationDocument("orders/order", requestFields));
	}

	@Test
	@DisplayName("[GET] [/orders/{id}] 단일 주문 조회 테스트 및 문서화")
	void getOrderDetail() throws Exception {
		PathParam pathParam = new PathParam("id", "주문 ID");
		ResponseFields responseFields = new ResponseFields(Map.of(
			"orderId", "주문 아이디",
			"uuid", "주문 고유 번호",
			"orderStatus", "주문 상태",
			"totalPrice", "주문 총 가격",
			"orderItems[].id", "주문 상품 ID",
			"orderItems[].productId", "주문 상품 ID",
			"orderItems[].name", "주문 상품 이름",
			"orderItems[].image", "주문 상품 이미지 URL",
			"orderItems[].price", "주문 상품 가격",
			"orderItems[].quantity", "주문 상품 수량"
		));

		String token = getToken();
		Long orderId = 1L;
		OrderDetailResponse response = OrderDetailResponse.from(getOrder());
		given(orderService.findOrderByOrderIdAndMemberId(any(), any())).willReturn(response);

		mockMvc.perform(get("/orders/{id}", orderId)
				.header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("getOrderDetail"))
			.andDo(authorizationDocument("orders/detail", pathParam, responseFields));
	}

	@Test
	@DisplayName("[GET] [/orders] 주문 조회 테스트 및 문서화")
	void getOrderDetails() throws Exception {
		ResponseFields responseFields = new ResponseFields(Map.of(
			"[]orderId", "주문 아이디",
			"[]uuid", "주문 고유 번호",
			"[]orderStatus", "주문 상태",
			"[]totalPrice", "주문 총 가격",
			"[]orderItems[].id", "주문 상품 ID",
			"[]orderItems[].productId", "주문 상품 ID",
			"[]orderItems[].name", "주문 상품 이름",
			"[]orderItems[].image", "주문 상품 이미지 URL",
			"[]orderItems[].price", "주문 상품 가격",
			"[]orderItems[].quantity", "주문 상품 수량"
		));

		String token = getToken();
		OrderDetailResponse response = OrderDetailResponse.from(getOrder());
		given(orderService.findAllOrderByMemberId(any())).willReturn(List.of(response));

		mockMvc.perform(get("/orders")
				.header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("getOrderDetail"))
			.andDo(authorizationDocument("orders/details", responseFields));
	}

	private Order getOrder() {
		return Order.builder()
			.memberId(1L)
			.orderItems(List.of(
				getOrderItem(1L, "된장 밀키트", 15000L, 20),
				getOrderItem(2L, "닭갈비 밀키트", 30000L, 2)))
			.build();
	}

	private OrderItem getOrderItem(
		final Long productId,
		final String name,
		final long price,
		final int quantity
	) {
		return OrderItem.of(
			productId,
			name,
			Image.from("https://service-hub.org/file/log"),
			Money.from(price),
			Quantity.from(quantity));
	}
}

