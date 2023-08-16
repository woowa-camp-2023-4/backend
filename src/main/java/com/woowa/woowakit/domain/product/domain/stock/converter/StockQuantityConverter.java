package com.woowa.woowakit.domain.product.domain.stock.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.product.domain.stock.StockQuantity;

@Converter
public class StockQuantityConverter implements AttributeConverter<StockQuantity, Long> {
	@Override
	public Long convertToDatabaseColumn(final StockQuantity attribute) {
		return attribute.getQuantity();
	}

	@Override
	public StockQuantity convertToEntityAttribute(final Long dbData) {
		return StockQuantity.from(dbData);
	}
}
