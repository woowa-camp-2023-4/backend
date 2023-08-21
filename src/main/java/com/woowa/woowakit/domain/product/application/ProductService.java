package com.woowa.woowakit.domain.product.application;

import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductSearchRequest;
import com.woowa.woowakit.domain.product.dto.request.ProductStatusUpdateRequest;
import com.woowa.woowakit.domain.product.dto.response.ProductDetailResponse;
import com.woowa.woowakit.domain.product.exception.ProductNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ProductDetailResponse> searchProducts(final ProductSearchRequest productSearchRequest) {
        List<Product> products = productRepository.searchProducts(productSearchRequest.toProductSearchCondition());

        return products.stream()
            .map(ProductDetailResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void updateStatus(final Long id, final ProductStatusUpdateRequest request) {
        Product product = findProductById(id);
        product.updateProductStatus(request.getProductStatus());
    }

    private Product findProductById(final Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotExistException::new);
    }
}
