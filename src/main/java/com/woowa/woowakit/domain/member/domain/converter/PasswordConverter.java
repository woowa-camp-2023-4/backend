package com.woowa.woowakit.domain.member.domain.converter;

import com.woowa.woowakit.domain.member.domain.EncodedPassword;
import javax.persistence.AttributeConverter;

public class PasswordConverter implements AttributeConverter<EncodedPassword, String> {

    @Override
    public String convertToDatabaseColumn(EncodedPassword encodedPassword) {
        return encodedPassword.getValue();
    }

    @Override
    public EncodedPassword convertToEntityAttribute(String encodedPassword) {
        return EncodedPassword.from(encodedPassword);
    }
}
