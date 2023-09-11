package com.woowa.woowakit.shop.auth.domain.converter;

import com.woowa.woowakit.shop.auth.domain.EncodedPassword;

import javax.persistence.AttributeConverter;

public class PasswordConverter implements AttributeConverter<EncodedPassword, String> {

    @Override
    public String convertToDatabaseColumn(final EncodedPassword encodedPassword) {
        return encodedPassword.getValue();
    }

    @Override
    public EncodedPassword convertToEntityAttribute(final String encodedPassword) {
        return EncodedPassword.from(encodedPassword);
    }
}
