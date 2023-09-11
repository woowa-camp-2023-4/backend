package com.woowa.woowakit.shop.auth.domain.converter;

import com.woowa.woowakit.shop.auth.domain.Email;

import javax.persistence.AttributeConverter;

public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(final Email email) {
        return email.getValue();
    }

    @Override
    public Email convertToEntityAttribute(final String email) {
        return Email.from(email);
    }
}