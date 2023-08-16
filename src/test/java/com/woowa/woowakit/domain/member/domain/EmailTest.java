package com.woowa.woowakit.domain.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowa.woowakit.domain.member.exception.EmailInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Email 단위테스트")
class EmailTest {

    @Test
    @DisplayName("이메일을 생성하는데 성공한다.")
    void createEmail() {
        String value = "yongs170@naver.com";

        final Email email = Email.from(value);

        assertThat(email.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"yyy.com", "yyy@dasd", "yyy@", "@x."})
    @NullAndEmptySource
    @DisplayName("빈 문자열이나 null , 이메일 형식이 아니면 예외를 던진다.")
    void validateEmail(final String value) {
        assertThatThrownBy(() -> Email.from(value))
            .isInstanceOf(EmailInvalidException.class);
    }
}
