package com.woowa.woowakit.domain.product.dao;

import java.util.List;

import com.woowa.woowakit.domain.product.domain.product.AllProductSearchCondition;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.InStockProductSearchCondition;
import com.woowa.woowakit.domain.product.domain.product.ProductSpecification;

public interface ProductRepositoryCustom {

	List<ProductSpecification> searchInStockProducts(InStockProductSearchCondition condition);

	List<Product> searchAllProducts(AllProductSearchCondition condition);
}
