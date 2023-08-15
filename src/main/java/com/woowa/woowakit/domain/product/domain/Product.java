package com.woowa.woowakit.domain.product.domain;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Convert(converter = ProductNameConverter.class)
	private ProductName name;

	@Convert(converter = ProductPriceConverter.class)
	private ProductPrice price;
	private String imageUrl;
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
}
