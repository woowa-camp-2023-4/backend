package com.woowa.woowakit.domain.product.domain.product;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select p from Product p where p.id = :id")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Product> findByIdWithPessimistic(@Param("id") Long id);
}
