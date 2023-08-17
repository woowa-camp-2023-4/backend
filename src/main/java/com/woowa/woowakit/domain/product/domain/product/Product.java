package com.woowa.woowakit.domain.product.domain.product;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;
import com.woowa.woowakit.domain.product.domain.product.converter.ProductImageConverter;
import com.woowa.woowakit.domain.product.domain.product.converter.ProductNameConverter;
import com.woowa.woowakit.domain.product.domain.product.converter.ProductPriceConverter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

	private static final Quantity INITIAL_QUANTITY = Quantity.from(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Convert(converter = ProductNameConverter.class)
	private ProductName name;

	@Convert(converter = ProductPriceConverter.class)
	private ProductPrice price;

	@Convert(converter = ProductImageConverter.class)
	private ProductImage imageUrl;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	@Convert(converter = QuantityConverter.class)
	private Quantity quantity;

	@Builder
	private Product(
		final Long id,
		final ProductName name,
		final ProductPrice price,
		final ProductImage imageUrl,
		final ProductStatus status,
		final Quantity quantity
	) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.status = status;
		this.quantity = quantity;
	}

	public static Product of(
		final String name,
		final Long price,
		final String imageUrl
	) {
		return Product.builder()
			.name(ProductName.from(name))
			.price(ProductPrice.from(price))
			.imageUrl(ProductImage.from(imageUrl))
			.status(ProductStatus.PRE_REGISTRATION)
			.quantity(INITIAL_QUANTITY)
			.build();
	}

	public void addQuantity(final Quantity quantity) {
		this.quantity = this.quantity.add(quantity);
	}
}
