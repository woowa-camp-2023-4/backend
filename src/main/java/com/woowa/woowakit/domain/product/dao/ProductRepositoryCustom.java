package com.woowa.woowakit.domain.product.dao;

import java.util.List;

import com.woowa.woowakit.domain.product.domain.product.AdminProductSearchCondition;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductSearchCondition;
import com.woowa.woowakit.domain.product.domain.product.ProductSpecification;

public interface ProductRepositoryCustom {

	List<ProductSpecification> searchProducts(ProductSearchCondition condition);

	List<Product> searchAdminProducts(AdminProductSearchCondition condition);
}
