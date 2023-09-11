package com.woowa.woowakit.shop.auth.domain;

import com.woowa.woowakit.shop.auth.exception.EmailInvalidException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Email {

    private static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final String value;

    private Email(final String value) {
        validate(value);
        this.value = value;
    }

    public static Email from(final String value) {
        return new Email(value);
    }

    private void validate(final String value) {
        if (value == null || !value.matches(EMAIL_REGEX_PATTERN)) {
            throw new EmailInvalidException();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Email{" +
                "value='" + value + '\'' +
                '}';
    }
}
