package com.woowa.woowakit.domain.model;

import javax.persistence.AttributeConverter;

public class MoneyConverter implements AttributeConverter<Money, Long> {

	@Override
	public Long convertToDatabaseColumn(Money attribute) {
		return attribute.getValue();
	}

	@Override
	public Money convertToEntityAttribute(Long dbData) {
		return Money.of(dbData);
	}
}
