package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.stock.converter.ExpiryDateConverter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STOCKS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stock extends BaseEntity {

	private static final int DAYS = 6;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Convert(converter = ExpiryDateConverter.class)
	private ExpiryDate expiryDate;

	@Convert(converter = QuantityConverter.class)
	private Quantity quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@Enumerated(EnumType.STRING)
	private StockType stockType;

	@Builder
	private Stock(
		final Long id,
		final ExpiryDate expiryDate,
		final Quantity quantity,
		final Product product
	) {
		this.id = id;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.product = product;
		this.stockType = StockType.NORMAL;
	}

	public static Stock of(
		final LocalDate expiryDate,
		final Product product
	) {
		return Stock.builder()
			.expiryDate(ExpiryDate.from(expiryDate))
			.quantity(Quantity.from(0))
			.product(product)
			.build();
	}

	public void CheckExpiredExpiryDate(final LocalDate currentDate) {
		LocalDate expiredExpiryDate = currentDate.plusDays(DAYS);
		if (isExpiredExpiryDate(expiredExpiryDate)) {
			this.stockType = StockType.EXPIRED;
		}
	}

	private boolean isExpiredExpiryDate(LocalDate expiredExpiryDate) {
		return expiredExpiryDate.isAfter(expiryDate.getDate())
			|| expiredExpiryDate.isEqual(expiryDate.getDate());
	}

	public boolean isEmpty() {
		return this.quantity.isEmpty();
	}

	public void addQuantity(final Quantity quantity) {
		this.quantity = this.quantity.add(quantity);
		this.product.addQuantity(quantity);
	}

	public void subtractQuantity(final Quantity quantity) {
		this.quantity = this.quantity.subtract(quantity);
	}
}
