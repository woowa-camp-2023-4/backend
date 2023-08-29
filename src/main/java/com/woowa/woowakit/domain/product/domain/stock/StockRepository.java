package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woowa.woowakit.domain.model.Quantity;

public interface StockRepository extends JpaRepository<Stock, Long> {

	Optional<Stock> findByProductIdAndExpiryDate(Long productId, ExpiryDate expiryDate);

	@Query(" select s from Stock s "
		+ " where s.product.id = :productId and s.stockType = :type "
		+ " order by s.expiryDate asc ")
	List<Stock> findAllByProductId(@Param("productId") Long productId, @Param("type") StockType stockType);

	@Modifying
	@Query("update Stock s set s.stockType = :type where s.expiryDate = :currentDate and s.product.id = :productId")
	void updateStatus(@Param("type") StockType stockType, @Param("currentDate") ExpiryDate currentDate,
		@Param("productId") Long productId);

	@Modifying
	@Query("delete FROM Stock s where s.id in :ids")
	void deleteStock(@Param("ids") List<Long> ids);

	@Modifying
	@Query("update  Stock s set s.quantity = :quantity where s.id = :id")
	void updateStockQuantity(@Param("id") Long stockId, @Param("quantity") Quantity quantity);

	@Query(value = "select sum(s.quantity) from stocks s where s.product_id = :productId "
		+ " and s.stock_type =:type "
		+ " and s.expiry_date =:expiryDate", nativeQuery = true)
	Optional<Long> countStockByExpiry(@Param("productId") Long productId, @Param("type") String stockType,
		@Param("expiryDate") LocalDate currentDate);
}
