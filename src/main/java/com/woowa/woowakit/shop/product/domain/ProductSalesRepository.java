package com.woowa.woowakit.shop.product.domain;

import java.util.List;

import com.woowa.woowakit.shop.product.domain.product.ProductSales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {
	List<ProductSales> findByProductId(Long productId);
}
