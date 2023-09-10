package com.woowa.woowakit.domains.member.application;

import com.woowa.woowakit.domains.auth.application.PasswordValidator;
import com.woowa.woowakit.domains.auth.exception.PasswordInvalidException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PasswordValidator 단위 테스트")
class PasswordValidatorTest {


    @ParameterizedTest
    @ValueSource(strings = {"passwor", "1234567890password"})
    @DisplayName("비밀번호가 소문자를 포함한 7자 이상 18자 이하의 이루어져 있으면 성공한다")
    void validatePasswordSuccess(final String password) {
        PasswordValidator passwordValidator = new PasswordValidator();

        Assertions.assertThatCode(() -> passwordValidator.validatePassword(password))
            .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"passwo", "1234567890password1"})
    @DisplayName("비밀번호가 소문자를 포함한 7자 이상 18자 이하의 이루어져 있지 않으면 예외가 발생한다.")
    void validatePasswordThrowException(final String password) {
        PasswordValidator passwordValidator = new PasswordValidator();

        Assertions.assertThatCode(() -> passwordValidator.validatePassword(password))
            .isInstanceOf(PasswordInvalidException.class);
    }
}
