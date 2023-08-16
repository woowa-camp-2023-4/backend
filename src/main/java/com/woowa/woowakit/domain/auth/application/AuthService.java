package com.woowa.woowakit.domain.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domain.auth.domain.*;
import com.woowa.woowakit.domain.auth.dto.request.LoginRequest;
import com.woowa.woowakit.domain.auth.dto.request.SignUpRequest;
import com.woowa.woowakit.domain.auth.dto.response.LoginResponse;
import com.woowa.woowakit.domain.auth.exception.LoginFailException;
import com.woowa.woowakit.domain.auth.infra.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public Long signUp(final SignUpRequest request) {
        passwordValidator.validatePassword(request.getPassword());

        final Member member = Member.of(
                request.getEmail(),
                EncodedPassword.of(request.getPassword(), passwordEncoder),
                request.getName()
        );

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public LoginResponse loginMember(final LoginRequest loginRequest) {
        final Member member = getMemberByEmail(loginRequest.getEmail());
        member.validatePassword(loginRequest.getPassword(), passwordEncoder);
        final AuthPrincipal authPrincipal = AuthPrincipal.from(member);

        return LoginResponse.from(tokenProvider.createToken(principalToJson(authPrincipal)));
    }

    private String principalToJson(final AuthPrincipal authPrincipal) {
        try {
            return objectMapper.writeValueAsString(authPrincipal);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private Member getMemberByEmail(final String email) {
        return memberRepository.findByEmail(Email.from(email))
                .orElseThrow(LoginFailException::new);
    }
}
