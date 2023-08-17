package com.woowa.woowakit.domain.order.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.order.domain.TotalPrice;

@Converter
public class TotalPriceConverter implements AttributeConverter<TotalPrice, Long> {
	@Override
	public Long convertToDatabaseColumn(TotalPrice attribute) {
		return attribute.getMoney().getValue();
	}

	@Override
	public TotalPrice convertToEntityAttribute(Long dbData) {
		return TotalPrice.from(dbData);
	}
}
