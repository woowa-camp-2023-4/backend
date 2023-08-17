package com.woowa.woowakit.domain.product.domain.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.product.domain.product.ProductName;

@Converter
public class ProductNameConverter implements AttributeConverter<ProductName, String> {

	@Override
	public String convertToDatabaseColumn(final ProductName attribute) {
		return attribute.getName();
	}

	@Override
	public ProductName convertToEntityAttribute(final String dbData) {
		return ProductName.from(dbData);
	}
}
