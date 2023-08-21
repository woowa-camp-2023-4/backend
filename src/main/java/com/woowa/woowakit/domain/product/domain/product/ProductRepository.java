package com.woowa.woowakit.domain.product.domain.product;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.dao.ProductRepositoryCustom;

import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("select p.quantity from Product p where p.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Quantity> findQuantityCountById(@Param("id") Long id);
}
