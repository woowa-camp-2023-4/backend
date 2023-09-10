package com.woowa.woowakit.domains.product.domain;

import java.util.List;

import com.woowa.woowakit.domains.product.domain.product.ProductSales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {
	List<ProductSales> findByProductId(Long productId);
}
