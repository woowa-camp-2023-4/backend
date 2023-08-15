package com.woowa.woowakit.domain.product.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.model.Money;

@Converter
public class ProductPriceConverter implements AttributeConverter<ProductPrice, Long> {

	@Override
	public Long convertToDatabaseColumn(final ProductPrice attribute) {
		return attribute.getPrice().getValue();
	}

	@Override
	public ProductPrice convertToEntityAttribute(final Long dbData) {
		return ProductPrice.from(Money.from(dbData));
	}
}