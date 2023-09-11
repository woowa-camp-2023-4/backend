package com.woowa.woowakit.shop.auth.application;

import com.woowa.woowakit.shop.auth.exception.PasswordInvalidException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.{7,18}$).*";

    public void validatePassword(final String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new PasswordInvalidException();
        }
    }
}