package com.woowa.woowakit.domain.product.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.woowakit.domain.product.application.ProductService;
import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody final ProductCreateRequest request) {
		final Long id = productService.create(request);
		return ResponseEntity.created(URI.create("/products/" + id)).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDetailResponse> findById(@PathVariable final Long id) {
		final ProductDetailResponse response = productService.findById(id);
		return ResponseEntity.ok(response);
	}
}
