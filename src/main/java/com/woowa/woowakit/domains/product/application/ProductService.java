package com.woowa.woowakit.domains.product.application;

import com.woowa.woowakit.domains.product.domain.product.InStockProductSearchCondition;
import com.woowa.woowakit.domains.product.domain.product.Product;
import com.woowa.woowakit.domains.product.domain.product.ProductRepository;
import com.woowa.woowakit.domains.product.domain.product.ProductSpecification;
import com.woowa.woowakit.domains.product.dto.request.AllProductSearchRequest;
import com.woowa.woowakit.domains.product.dto.request.InStockProductSearchRequest;
import com.woowa.woowakit.domains.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domains.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.domains.product.dto.response.ProductDetailResponse;
import com.woowa.woowakit.domains.product.dto.response.ProductResponse;
import com.woowa.woowakit.domains.product.exception.ProductNotExistException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
	@Cacheable(value = "productsFirstRanking")
	public List<ProductResponse> findRankingProducts() {
		final List<ProductSpecification> productSpecifications = productRepository.searchInStockProducts(
			InStockProductSearchCondition.builder().build());
		log.info("상위 랭크 상품 조회 캐시 미적용");
		return ProductResponse.listOfProductSpecification(productSpecifications);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> searchAllProducts(final AllProductSearchRequest request) {
		List<Product> products = productRepository.searchAllProducts(
			request.toAllProductSearchCondition());
		return ProductResponse.listOfProducts(products);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> searchInStockProducts(final InStockProductSearchRequest request) {
		final List<ProductSpecification> productSpecifications = productRepository.searchInStockProducts(
			request.toInStockProductSearchCondition());

		return ProductResponse.listOfProductSpecification(productSpecifications);
	}

	@Transactional
	@CacheEvict(cacheNames = "productsFirstRanking", allEntries = true)
	public void updateStatus(final Long id, final ProductStatusUpdateRequest request) {
		Product product = findProductById(id);
		log.info(
			"ProductService.updateStatus() 로직 실행 전: productId = {}, status = {}, updateStatus = {}",
			id, product.getStatus().name(), request.getProductStatus().name());
		product.updateProductStatus(request.getProductStatus());
		log.info("ProductService.updateStatus() 로직 실행 후: productId = {}, status = {}", id,
			product.getStatus().name());
	}

	private Product findProductById(final Long id) {
		return productRepository.findById(id).orElseThrow(ProductNotExistException::new);
	}
}
