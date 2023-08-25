package com.woowa.woowakit.domain.product.domain.product;

import java.time.LocalDate;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;

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

	private Long productId;

	@Convert(converter = QuantityConverter.class)
	private Quantity sale;

	private LocalDate saleDate;

	@Builder
	public ProductSales(Long productId, Quantity sale, LocalDate saleDate) {
		this.productId = productId;
		this.sale = sale;
		this.saleDate = saleDate;
	}
}
