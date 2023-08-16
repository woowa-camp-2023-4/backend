package com.woowa.woowakit.domain.product.domain.stock.converter;

import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.product.domain.stock.ExpiryDate;

@Converter
public class ExpiryDateConverter implements AttributeConverter<ExpiryDate, LocalDate> {
	@Override
	public LocalDate convertToDatabaseColumn(final ExpiryDate attribute) {
		return attribute.getDate();
	}

	@Override
	public ExpiryDate convertToEntityAttribute(final LocalDate dbData) {
		return ExpiryDate.from(dbData);
	}
}
