package com.woowa.woowakit.domain.product.domain.stock;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

	Optional<Stock> findByProductIdAndExpiryDate(Long productId, ExpiryDate expiryDate);

	@Query(" select s from Stock s"
		+ "  join s.product p "
		+ "  where p.id = :productId and s.stockType = :type "
		+ "  order by s.expiryDate asc ")
	List<Stock> findAllByProductId(@Param("productId") Long productId, @Param("type") StockType stockType);
}
