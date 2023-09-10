package com.woowa.woowakit.domains.order.api;

import static com.woowa.woowakit.restDocsHelper.RestDocsHelper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domains.model.Image;
import com.woowa.woowakit.domains.model.Money;
import com.woowa.woowakit.domains.model.Quantity;
import com.woowa.woowakit.domains.order.application.OrderService;
import com.woowa.woowakit.domains.order.domain.Order;
import com.woowa.woowakit.domains.order.domain.OrderItem;
import com.woowa.woowakit.domains.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domains.order.dto.request.OrderPayRequest;
import com.woowa.woowakit.domains.order.dto.request.OrderSearchRequest;
import com.woowa.woowakit.domains.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domains.order.dto.response.OrderResponse;
import com.woowa.woowakit.restDocsHelper.PathParam;
import com.woowa.woowakit.restDocsHelper.RequestFields;
import com.woowa.woowakit.restDocsHelper.ResponseFields;
import com.woowa.woowakit.restDocsHelper.RestDocsTest;

@WebMvcTest(OrderController.class)
@AutoConfigureRestDocs(uriHost = "api.test.com", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
class OrderControllerTest extends RestDocsTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper autowiredObjectMapper;

	@Test
	@DisplayName("[POST] [/orders] 주문 생성 테스트 및 문서화")
	void createPreOrder() throws Exception {
		RequestFields requestFields = new RequestFields(Map.of(
			"[]productId", "주문 상품 ID",
			"[]quantity", "주문 상품 수량"
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
		List<OrderCreateRequest> request = List.of(
			OrderCreateRequest.of(1L, 10L),
			OrderCreateRequest.of(2L, 10L)
		);
		OrderResponse response = OrderResponse.from(getOrder());
		given(orderService.create(any(), any())).willReturn(response);

		mockMvc.perform(post("/orders")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(handler().methodName("create"))
			.andDo(authorizationDocument("orders/create", requestFields, responseFields));
	}

	@Test
	@DisplayName("[POST] [/orders/{id}/pay] 주문 결제 테스트 및 문서화")
	void createOrder() throws Exception {
		PathParam pathParam = new PathParam("id", "주문 ID");
		RequestFields requestFields = new RequestFields(Map.of(
			"paymentKey", "결제 키"
		));

		String token = getToken();
		OrderPayRequest request = OrderPayRequest.of("paymentKey");

		mockMvc.perform(post("/orders/{id}/pay", 1L)
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("pay"))
			.andDo(authorizationDocument("orders/pay", pathParam, requestFields));
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
		RequestFields requestFields = new RequestFields(Map.of(
			"lastOrderId", "마지막 주문 ID",
			"pageSize", "페이지 사이즈 (기본 20)"
		));
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
		given(orderService.findAllOrderByMemberId(any(), any())).willReturn(List.of(response));

		mockMvc.perform(get("/orders")
				.content(autowiredObjectMapper.writeValueAsString(OrderSearchRequest.of(100L, 20)))
				.header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("getOrderDetail"))
			.andDo(authorizationDocument("orders/details", requestFields, responseFields));
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

