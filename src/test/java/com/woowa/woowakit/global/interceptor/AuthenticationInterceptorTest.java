package com.woowa.woowakit.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.shop.auth.domain.MemberRepository;
import com.woowa.woowakit.shop.auth.infra.TokenProvider;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;
import com.woowa.woowakit.global.error.TokenInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthInterceptorTest 단위 테스트")
@DataJpaTest
@Import({QuerydslTestConfig.class, JpaConfig.class})
class AuthenticationInterceptorTest {

    public static final String LONG_EXPIRED_TOKEN = "test";
    public static final String SECRET_KEY = "fjhbewhjbrfkelwdvhkewjkbwd";
    public static final long VALIDITY_IN_MILLISECONDS = 1000L;
    public static final String BEARER_TYPE = "Bearer";

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Authorization header에 토큰이 없는 경우 true 반환한다.")
    void preHandleWhenNoToken() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        TokenProvider tokenProvider = new TokenProvider(SECRET_KEY, VALIDITY_IN_MILLISECONDS);
        AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(
                tokenProvider,
                memberRepository,
                new ObjectMapper()
        );

        assertThat(authenticationInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null))
                .isTrue();
    }

    @Test
    @DisplayName("Authorization header에 토큰이 비정상인 경우 예외를 반환한다")
    void preHandleWhenInvalidToken() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION,
                BEARER_TYPE + " " + "invalid token");
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        TokenProvider tokenProvider = new TokenProvider(SECRET_KEY, VALIDITY_IN_MILLISECONDS);
        AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(
                tokenProvider,
                memberRepository,
                new ObjectMapper()
        );

        assertThatThrownBy(() -> authenticationInterceptor.preHandle(
                mockHttpServletRequest, mockHttpServletResponse, null))
                .isInstanceOf(TokenInvalidException.class);
    }
}

