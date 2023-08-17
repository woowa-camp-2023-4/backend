package com.woowa.woowakit.domain.product.domain.stock.converter;

import java.sql.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.product.domain.stock.ExpiryDate;

@Converter
public class ExpiryDateConverter implements AttributeConverter<ExpiryDate, Date> {

	@Override
	public Date convertToDatabaseColumn(final ExpiryDate attribute) {
		return Date.valueOf(attribute.getDate());
	}

	@Override
	public ExpiryDate convertToEntityAttribute(final Date dbData) {
		return ExpiryDate.from(dbData.toLocalDate());
	}
}
