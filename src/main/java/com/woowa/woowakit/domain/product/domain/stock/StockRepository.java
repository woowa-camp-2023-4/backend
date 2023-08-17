package com.woowa.woowakit.domain.product.domain.stock;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductIdAndExpiryDate(Long productId, ExpiryDate expiryDate);

    @Query("select s from Stock s join fetch s.product where s.product.id = :productId")
    List<Stock> findAllByProductId(Long productId);
}
