package com.woowa.woowakit.domains.member.domain;

import com.woowa.woowakit.domains.auth.domain.EncodedPassword;
import com.woowa.woowakit.domains.auth.domain.Member;
import com.woowa.woowakit.domains.auth.domain.PasswordEncoder;
import com.woowa.woowakit.domains.auth.exception.LoginFailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("패스워드가 일치하지 않으면 예외를 던진다.")
    void validatePassword() {
        Member member = Member.of(
            "yongs@naver.com",
            EncodedPassword.from("1234111"),
            "yong"
        );

        PasswordEncoder passwordEncoder = new PasswordEncoder() {
            @Override
            public boolean matches(String planePassword, String encodedPassword) {
                return false;
            }

            @Override
            public String encode(String password) {
                return "1234111";
            }
        };

        Assertions.assertThatThrownBy(() -> member.validatePassword("43222221", passwordEncoder))
            .isInstanceOf(LoginFailException.class);
    }
}
