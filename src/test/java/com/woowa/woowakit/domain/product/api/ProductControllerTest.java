package com.woowa.woowakit.domain.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.application.ProductService;
import com.woowa.woowakit.domain.product.application.StockService;
import com.woowa.woowakit.domain.product.domain.product.*;
import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductSearchRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;
import com.woowa.woowakit.domain.product.dto.response.ProductResponse;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.woowa.woowakit.restDocsHelper.RestDocsHelper.authorizationDocument;
import static com.woowa.woowakit.restDocsHelper.RestDocsHelper.defaultDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureRestDocs(uriHost = "api.test.com", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
class ProductControllerTest extends RestDocsTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@MockBean
	private StockService stockService;

	@Autowired
	private ObjectMapper autowiredObjectMapper;

	@Test
	@DisplayName("[POST] [/products] 상품 생성 테스트 및 문서화")
	void create() throws Exception {
		RequestFields requestFields = new RequestFields(Map.of(
			"name", "상품 이름",
			"price", "상품 가격",
			"imageUrl", "상품 이미지 URL"
		));

		String token = getToken();
		ProductCreateRequest request = ProductCreateRequest.of(
			"파스타 밀키트",
			50000L,
			"https://service-hub.org/file/log");
		given(productService.create(any())).willReturn(1L);

		mockMvc.perform(post("/products")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(handler().methodName("create"))
			.andDo(authorizationDocument("product/create", requestFields));
	}

	@Test
	@DisplayName("[GET] [/products/{id}] 상품 단일 조회  테스트 및 문서화")
	void findById() throws Exception {
		PathParam pathParam = new PathParam("id", "상품 ID");
		ResponseFields responseFields = new ResponseFields(Map.of(
			"name", "상품 이름",
			"id", "상품 ID",
			"price", "상품 가격",
			"imageUrl", "상품 이미지 URL",
			"status", "상품 상태",
			"quantity", "상품 총 수량"
		));

		Long productId = 1L;
		ProductDetailResponse response = ProductDetailResponse.from(getProduct());
		given(productService.findById(productId)).willReturn(response);

		mockMvc.perform(get("/products/{id}", productId))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("findById"))
			.andDo(defaultDocument("product/findById", pathParam, responseFields));
	}

	@Test
	@DisplayName("[GET] [/products/rank] 상품 첫 페이지 조회 테스트 및 문서화")
	void searchMainPage() throws Exception {
		ResponseFields responseFields = new ResponseFields(Map.of(
			"[]name", "상품 이름",
			"[]id", "상품 ID",
			"[]price", "상품 가격",
			"[]imageUrl", "상품 이미지 URL",
			"[]status", "상품 상태",
			"[]quantity", "상품 총 수량",
			"[]productSale", "마지막 상품 판매량"
		));

		List<ProductResponse> response = List.of(
			ProductResponse.from(getProductSpecification()));
		given(productService.findRankingProducts()).willReturn(response);

		mockMvc.perform(get("/products/rank"))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("getMainPage"))
			.andDo(defaultDocument("product/main", responseFields));
	}

	@Test
	@DisplayName("[GET] [/products] 상품 전체 조회 테스트 및 문서화")
	void searchProducts() throws Exception {
		RequestFields requestFields = new RequestFields(Map.of(
			"productKeyword", "상품 이름 키워드",
			"lastProductId", "마지막 상품 ID",
			"pageSize", "페이지 사이즈",
			"saleDate", "판매 일자",
			"lastProductSale", "마지막 상품 판매량"
		));
		ResponseFields responseFields = new ResponseFields(Map.of(
			"[]name", "상품 이름",
			"[]id", "상품 ID",
			"[]price", "상품 가격",
			"[]imageUrl", "상품 이미지 URL",
			"[]status", "상품 상태",
			"[]quantity", "상품 총 수량",
			"[]productSale", "마지막 상품 판매량"
		));

		ProductSearchRequest request = ProductSearchRequest.of(
			"",
			3L,
			109L,
			10,
			LocalDate.now());
		List<ProductResponse> response = List.of(
			ProductResponse.from(getProductSpecification()));
		given(productService.searchProducts(any())).willReturn(response);

		mockMvc.perform(get("/products/search")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.content(autowiredObjectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("searchProducts"))
			.andDo(defaultDocument("product/findAll", requestFields, responseFields));
	}

	@Test
	@DisplayName("[PATCH] [/products/{id}/status] 상품 상태 테스트 및 문서화")
	void updateStatus() throws Exception {
		PathParam pathParam = new PathParam("id", "재고 ID");
		RequestFields requestFields = new RequestFields(Map.of(
			"productStatus", "상품 상태"
		));

		Long productId = 1L;
		String token = getToken();
		ProductStatusUpdateRequest request = ProductStatusUpdateRequest.of(ProductStatus.IN_STOCK);
		willDoNothing().given(productService).updateStatus(any(), any());

		mockMvc.perform(patch("/products/{id}/status", productId)
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(autowiredObjectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent())
			.andExpect(handler().methodName("updateStatus"))
			.andDo(authorizationDocument("product/status", pathParam, requestFields));
	}

	@Test
	@DisplayName("[POST] [/products/{id}/stocks] 상품 재고 추가 테스트 및 문서화")
	void addStock() throws Exception {
		PathParam pathParam = new PathParam("id", "재고 ID");
		RequestFields requestFields = new RequestFields(Map.of(
			"expiryDate", "유통 기한",
			"quantity", "재고 수량"
		));

		Long stockId = 1L;
		String token = getToken();
		StockCreateRequest request = StockCreateRequest.of(LocalDate.of(2025, 1, 1), 10000L);
		given(stockService.create(any(), any())).willReturn(stockId);

		mockMvc.perform(post("/products/{id}/stocks", stockId)
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(autowiredObjectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(handler().methodName("addStock"))
			.andDo(authorizationDocument("stock/add", pathParam, requestFields));
	}

	private ProductSpecification getProductSpecification() {
		return ProductSpecification.of(getProduct(), 109);
	}

	private Product getProduct() {
		return Product.builder()
			.status(ProductStatus.IN_STOCK)
			.quantity(Quantity.from(1000))
			.price(ProductPrice.from(10000L))
			.name(ProductName.from("된장 밀키트"))
			.imageUrl(ProductImage.from("https://service-hub/file/log/"))
			.build();
	}
}
