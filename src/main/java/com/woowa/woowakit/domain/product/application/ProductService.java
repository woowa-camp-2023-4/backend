package com.woowa.woowakit.domain.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;
import com.woowa.woowakit.domain.product.exception.ProductNotExistException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public Long create(final ProductCreateRequest request) {
		return productRepository.save(request.toEntity()).getId();
	}

	@Transactional(readOnly = true)
	public ProductDetailResponse findById(final Long id) {
		final Product product = findProductById(id);
		return ProductDetailResponse.from(product);
	}

	@Transactional(readOnly = true)
	public List<ProductDetailResponse> findAll() {
		return productRepository.findAll()
			.stream()
			.map(ProductDetailResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}

	private Product findProductById(final Long id) {
		return productRepository.findById(id).orElseThrow(ProductNotExistException::new);
	}
}
