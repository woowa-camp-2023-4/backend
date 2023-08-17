package com.woowa.woowakit.domain.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.model.Quantity;

@Converter
public class QuantityConverter implements AttributeConverter<Quantity, Long> {

	@Override
	public Long convertToDatabaseColumn(final Quantity attribute) {
		return attribute.getQuantity();
	}

	@Override
	public Quantity convertToEntityAttribute(final Long dbData) {
		return Quantity.from(dbData);
	}
}
