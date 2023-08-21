package com.woowa.woowakit.domain.product.dao;

import java.util.List;

import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductSearchCondition;

public interface ProductRepositoryCustom {

	List<Product> searchProducts(ProductSearchCondition condition);
}
