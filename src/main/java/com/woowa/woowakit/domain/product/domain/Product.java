package com.woowa.woowakit.domain.product.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Convert(converter = ProductNameConverter.class)
	private ProductName name;

	@Convert(converter = ProductPriceConverter.class)
	private ProductPrice price;

	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	@Builder
	private Product(
		final Long id,
		final ProductName name,
		final ProductPrice price,
		final String imageUrl,
		final ProductStatus status
	) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.status = status;
	}

	public static Product of(
		final String name,
		final Long price,
		final String imageUrl
	) {
		return Product.builder()
			.name(ProductName.from(name))
			.price(ProductPrice.from(price))
			.imageUrl(imageUrl)
			.status(ProductStatus.PRE_REGISTRATION)
			.build();
	}
}
