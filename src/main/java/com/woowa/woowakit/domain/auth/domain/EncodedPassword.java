package com.woowa.woowakit.domain.auth.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class EncodedPassword {

    private final String value;

    private EncodedPassword(final String encodedPassword) {
        this.value = encodedPassword;
    }

    public static EncodedPassword from(final String encodedPassword) {
        return new EncodedPassword(encodedPassword);
    }

    public static EncodedPassword of(final String rawPassword, final PasswordEncoder passwordEncoder) {
        return new EncodedPassword(passwordEncoder.encode(rawPassword));
    }

    public boolean isMatch(final String rawPassword, final PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EncodedPassword)) {
            return false;
        }
        EncodedPassword that = (EncodedPassword) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "Password{" +
                "value='" + value + '\'' +
                '}';
    }
}
