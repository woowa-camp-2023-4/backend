package com.woowa.woowakit.domains.product.domain.product;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woowa.woowakit.domains.product.dao.ProductRepositoryCustom;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

	@Query("select p from Product p where p.id = :id")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Product> findByIdWithPessimistic(@Param("id") Long id);

	@Query("select p from Product p where p.id in :ids")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Product> findByIdsWithPessimistic(@Param("ids") List<Long> ids);

	@Query("select p.id from Product p ")
	List<Long> findAllIds();
}
