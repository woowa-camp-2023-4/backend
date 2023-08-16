package com.woowa.woowakit.domain.member.domain.converter;

import com.woowa.woowakit.domain.member.domain.Email;
import javax.persistence.AttributeConverter;

public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email.getValue();
    }

    @Override
    public Email convertToEntityAttribute(String email) {
        return Email.from(email);
    }
}
