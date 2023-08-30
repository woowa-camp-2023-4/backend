package com.woowa.woowakit.domain.product.api;

import com.woowa.woowakit.domain.auth.annotation.Admin;
import com.woowa.woowakit.domain.product.application.ProductService;
import com.woowa.woowakit.domain.product.application.StockService;
import com.woowa.woowakit.domain.product.dto.request.AdminProductSearchRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductSearchRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;
import com.woowa.woowakit.domain.product.dto.response.ProductResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final StockService stockService;

	@Admin
	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody final ProductCreateRequest request) {
		log.info("상품 생성 name: {}, price: {} ", request.getName(), request.getPrice());
		final Long id = productService.create(request);
		return ResponseEntity.created(URI.create("/products/" + id)).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDetailResponse> findById(@PathVariable final Long id) {
		log.info("productId: {} 상품 조회", id);
		final ProductDetailResponse response = productService.findById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/rank")
	public ResponseEntity<List<ProductResponse>> getMainPage() {
		log.info("랭킹 상품 조회");
		List<ProductResponse> response = productService.findRankingProducts();
		return ResponseEntity.ok(response);
	}

	@Admin
	@GetMapping("/products")
	public ResponseEntity<List<ProductResponse>> searchAdminProducts(
		@Valid @ModelAttribute final AdminProductSearchRequest request
	) {
		log.info("관리자 상품 조회");
		List<ProductResponse> response = productService.searchAdminProducts(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductResponse>> searchProducts(
		@Valid @ModelAttribute final ProductSearchRequest request) {
		log.info("productKeyword: {}, lastProductId: {} 상품 조회", request.getProductKeyword(),
			request.getLastProductId());
		final List<ProductResponse> response = productService.searchProducts(request);
		return ResponseEntity.ok(response);
	}

	@Admin
	@PostMapping("/{id}/stocks")
	public ResponseEntity<Void> addStock(
		@PathVariable final Long id,
		@Valid @RequestBody final StockCreateRequest request
	) {
		log.info(
			"[Request] ProductController.addStock(): productId = {}, addStock = [expiryDate:{}, quantity: {}]",
			id,
			request.getExpiryDate(), request.getQuantity());
		long resultId = stockService.create(request, id);
		return ResponseEntity.created(URI.create("/products/" + id + "/stocks/" + resultId))
			.build();
	}

	@Admin
	@PatchMapping("/{id}/status")
	public ResponseEntity<Void> updateStatus(
		@PathVariable final Long id,
		@Valid @RequestBody final ProductStatusUpdateRequest request
	) {
		log.info("[Request] ProductController.updateStatus(): productId = {}, productStatus = {}",
			id,
			request.getProductStatus().name());
		productService.updateStatus(id, request);
		return ResponseEntity.noContent().build();
	}
}
