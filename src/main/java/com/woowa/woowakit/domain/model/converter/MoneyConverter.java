package com.woowa.woowakit.domain.model.converter;

import com.woowa.woowakit.domain.model.Money;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MoneyConverter implements AttributeConverter<Money, Long> {

    @Override
    public Long convertToDatabaseColumn(final Money attribute) {
        return attribute.getValue();
    }

    @Override
    public Money convertToEntityAttribute(final Long dbData) {
        return Money.from(dbData);
    }
}
