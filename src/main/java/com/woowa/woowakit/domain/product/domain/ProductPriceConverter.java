package com.woowa.woowakit.domain.product.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.model.Money;

@Converter
public class ProductPriceConverter implements AttributeConverter<ProductPrice, Money> {

	@Override
	public Money convertToDatabaseColumn(ProductPrice attribute) {
		return attribute.getPrice();
	}

	@Override
	public ProductPrice convertToEntityAttribute(Money dbData) {
		return ProductPrice.of(dbData);
	}
}
