package com.woowa.woowakit.domains.product.dao;

import java.util.List;

import com.woowa.woowakit.domains.product.domain.product.AllProductSearchCondition;
import com.woowa.woowakit.domains.product.domain.product.Product;
import com.woowa.woowakit.domains.product.domain.product.InStockProductSearchCondition;
import com.woowa.woowakit.domains.product.domain.product.ProductSpecification;

public interface ProductRepositoryCustom {

	List<ProductSpecification> searchInStockProducts(InStockProductSearchCondition condition);

	List<Product> searchAllProducts(AllProductSearchCondition condition);
}
