package com.woowa.woowakit.domain.product.domain.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.product.domain.product.ProductQuantity;

@Converter
public class ProductQuantityConverter implements AttributeConverter<ProductQuantity, Long> {

	@Override
	public Long convertToDatabaseColumn(final ProductQuantity attribute) {
		return attribute.getQuantity();
	}

	@Override
	public ProductQuantity convertToEntityAttribute(final Long dbData) {
		return ProductQuantity.from(dbData);
	}
}
