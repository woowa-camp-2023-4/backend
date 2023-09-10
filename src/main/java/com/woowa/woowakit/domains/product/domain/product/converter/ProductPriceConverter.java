package com.woowa.woowakit.domains.product.domain.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domains.model.Money;
import com.woowa.woowakit.domains.product.domain.product.ProductPrice;

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
