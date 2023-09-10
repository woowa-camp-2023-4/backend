package com.woowa.woowakit.domains.member.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domains.auth.application.AuthService;
import com.woowa.woowakit.domains.auth.application.PasswordValidator;
import com.woowa.woowakit.domains.auth.dto.request.LoginRequest;
import com.woowa.woowakit.domains.auth.exception.LoginFailException;
import com.woowa.woowakit.domains.auth.infra.PBKDF2PasswordEncoder;
import com.woowa.woowakit.domains.auth.infra.TokenProvider;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
	AuthService.class, PBKDF2PasswordEncoder.class,
	PasswordValidator.class,
	TokenProvider.class,
	ObjectMapper.class,
	QuerydslTestConfig.class,
	JpaConfig.class
})
class AuthServiceTest {

	@Autowired
	private AuthService authService;

	@Test
	@DisplayName("없는 이메일로 조회하면 예외를 반환한다.")
	void notFoundEmail() {
		Assertions.assertThatThrownBy(
				() -> authService.loginMember(LoginRequest.of("zzz@woowa.com", "asdfasdfasdf")))
			.isInstanceOf(LoginFailException.class);
	}
}
