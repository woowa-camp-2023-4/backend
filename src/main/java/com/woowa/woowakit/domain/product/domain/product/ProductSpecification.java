package com.woowa.woowakit.domain.product.domain.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductSpecification {

    private Product product;
    private long productSale;

    public static ProductSpecification of(final Product product, final long productSale) {
        return new ProductSpecification(product, productSale);
    }
}
