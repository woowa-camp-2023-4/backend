package com.woowa.woowakit.domain.product.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woowa.woowakit.domain.product.domain.product.ProductSales;

public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {
	List<ProductSales> findByProductId(Long productId);
}
