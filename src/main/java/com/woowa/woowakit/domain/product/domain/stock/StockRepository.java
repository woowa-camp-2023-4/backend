package com.woowa.woowakit.domain.product.domain.stock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
	Optional<Stock> findByProductIdAndExpiryDate(Long productId, ExpiryDate expiryDate);
}
