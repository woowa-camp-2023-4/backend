package com.woowa.woowakit.domain.product.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProductNameConverter implements AttributeConverter<ProductName, String> {

	@Override
	public String convertToDatabaseColumn(ProductName attribute) {
		return attribute.getName();
	}

	@Override
	public ProductName convertToEntityAttribute(String dbData) {
		return ProductName.of(dbData);
	}
}
