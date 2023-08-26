package com.woowa.woowakit.restDocsHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domain.auth.domain.*;
import com.woowa.woowakit.domain.auth.infra.TokenProvider;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public abstract class RestDocsTest {


    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private MemberRepository memberRepository;

    protected String getToken() throws JsonProcessingException {
        AuthPrincipal authPrincipal = getAuthPrincipal();
        String token = "Bearer token";
        given(tokenProvider.validateToken(any())).willReturn(true);
        given(tokenProvider.getPayload(any())).willReturn(getAuthPrincipalAsString(authPrincipal));
        given(memberRepository.existsById(any())).willReturn(true);
        return token;
    }

    protected String getAuthPrincipalAsString(final AuthPrincipal authPrincipal) throws JsonProcessingException {
        return objectMapper.writeValueAsString(authPrincipal);
    }

    protected AuthPrincipal getAuthPrincipal() {
        return AuthPrincipal.from(getMember());
    }

    protected Member getMember() {
        return Member.builder()
                .email(Email.from("yongs170@naver.com"))
                .role(Role.ADMIN)
                .name("tester")
                .build();
    }
}
