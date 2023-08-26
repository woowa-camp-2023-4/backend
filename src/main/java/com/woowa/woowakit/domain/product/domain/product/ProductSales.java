package com.woowa.woowakit.domain.product.domain.product;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Quantity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_sales")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductSales extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id")
	private Long productId;

	@Embedded
	private SaleQuantity sale;

	@Column(name = "sale_date")
	private LocalDate saleDate;

	@Builder
	public ProductSales(
		final Long productId,
		final Quantity sale,
		final LocalDate saleDate
	) {
		this.productId = productId;
		this.sale = SaleQuantity.from(sale.getValue());
		this.saleDate = saleDate;
	}
}
