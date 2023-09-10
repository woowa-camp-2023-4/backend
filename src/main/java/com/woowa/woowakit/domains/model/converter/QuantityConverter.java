package com.woowa.woowakit.domains.model.converter;

import com.woowa.woowakit.domains.model.Quantity;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class QuantityConverter implements AttributeConverter<Quantity, Long> {

    @Override
    public Long convertToDatabaseColumn(final Quantity attribute) {
        return attribute.getValue();
    }

    @Override
    public Quantity convertToEntityAttribute(final Long dbData) {
        return Quantity.from(dbData);
    }
}
