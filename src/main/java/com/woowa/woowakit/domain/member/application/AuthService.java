package com.woowa.woowakit.domain.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domain.member.domain.AuthPrincipal;
import com.woowa.woowakit.domain.member.domain.Email;
import com.woowa.woowakit.domain.member.domain.EncodedPassword;
import com.woowa.woowakit.domain.member.domain.Member;
import com.woowa.woowakit.domain.member.domain.MemberRepository;
import com.woowa.woowakit.domain.member.domain.PasswordEncoder;
import com.woowa.woowakit.domain.member.dto.request.LoginRequest;
import com.woowa.woowakit.domain.member.dto.request.SignUpRequest;
import com.woowa.woowakit.domain.member.dto.response.LoginResponse;
import com.woowa.woowakit.domain.member.exception.LoginFailException;
import com.woowa.woowakit.domain.member.exception.UnExpectedException;
import com.woowa.woowakit.domain.member.infra.TokenProvider;
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

        Member member = Member.of(
            request.getEmail(),
            EncodedPassword.of(request.getPassword(), passwordEncoder),
            request.getName()
        );

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public LoginResponse loginMember(LoginRequest loginRequest) {
        Member member = getMemberByEmail(loginRequest.getEmail());
        member.validatePassword(loginRequest.getPassword(), passwordEncoder);
        AuthPrincipal authPrincipal = AuthPrincipal.from(member);

        return LoginResponse.from(tokenProvider.createToken(principalToJson(authPrincipal)));
    }

    private String principalToJson(AuthPrincipal authPrincipal) {
        try {
            return objectMapper.writeValueAsString(authPrincipal);
        } catch (JsonProcessingException e) {
            throw new UnExpectedException(e);
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(Email.from(email))
            .orElseThrow(LoginFailException::new);
    }
}
