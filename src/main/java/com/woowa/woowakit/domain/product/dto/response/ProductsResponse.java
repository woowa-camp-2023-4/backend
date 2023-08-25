package com.woowa.woowakit.domain.product.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.woowa.woowakit.domain.product.domain.product.ProductSpecification;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductsResponse {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private String status;
    private long quantity;
    private long productSale;

    @Builder
    private ProductsResponse(
        final Long id,
        final String name,
        final Long price,
        final String imageUrl,
        final String status,
        final long quantity,
        final long productSale
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.quantity = quantity;
        this.productSale = productSale;
    }

    public static ProductsResponse from(final ProductSpecification productSpecification) {
        return ProductsResponse.builder()
            .id(productSpecification.getProduct().getId())
            .name(productSpecification.getProduct().getName().getName())
            .price(productSpecification.getProduct().getPrice().getPrice().getValue())
            .imageUrl(productSpecification.getProduct().getImageUrl().getPath())
            .quantity(productSpecification.getProduct().getQuantity().getValue())
            .status(productSpecification.getProduct().getStatus().name())
            .productSale(productSpecification.getProductSale())
            .build();
    }

    public static List<ProductsResponse> listOf(final List<ProductSpecification> productSpecifications) {
        return productSpecifications.stream()
            .map(ProductsResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }
}
