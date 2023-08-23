package com.woowa.woowakit.domain.product.domain.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.product.domain.product.ProductImage;

@Converter
public class ProductImageConverter implements AttributeConverter<ProductImage, String> {

	@Override
	public String convertToDatabaseColumn(final ProductImage attribute) {
		return attribute.getPath();
	}

	@Override
	public ProductImage convertToEntityAttribute(final String dbData) {
		return ProductImage.from(dbData);
	}
}
