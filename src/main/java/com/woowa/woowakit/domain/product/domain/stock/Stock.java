package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.stock.converter.ExpiryDateConverter;
import com.woowa.woowakit.domain.product.domain.stock.converter.StockQuantityConverter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STOCKS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Convert(converter = ExpiryDateConverter.class)
	private ExpiryDate expiryDate;

	@Convert(converter = StockQuantityConverter.class)
	private StockQuantity quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@Builder
	private Stock(
		final Long id,
		final ExpiryDate expiryDate,
		final StockQuantity quantity,
		final Product product
	) {
		this.id = id;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.product = product;
	}

	public static Stock of(
		final LocalDate expiryDate,
		final long quantity,
		final Product product
	) {
		return Stock.builder()
			.expiryDate(ExpiryDate.from(expiryDate))
			.quantity(StockQuantity.from(quantity))
			.product(product)
			.build();
	}
}
