package com.woowa.woowakit.domains.auth.application;

import com.woowa.woowakit.domains.auth.domain.PasswordEncoder;
import com.woowa.woowakit.domains.auth.dto.request.LoginRequest;
import com.woowa.woowakit.domains.auth.dto.request.SignUpRequest;
import com.woowa.woowakit.domains.auth.dto.response.LoginResponse;
import com.woowa.woowakit.domains.auth.exception.LoginFailException;
import com.woowa.woowakit.domains.auth.infra.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domains.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domains.auth.domain.Email;
import com.woowa.woowakit.domains.auth.domain.EncodedPassword;
import com.woowa.woowakit.domains.auth.domain.Member;
import com.woowa.woowakit.domains.auth.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

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
		Member member = getMemberByEmail(loginRequest.getEmail());
		member.validatePassword(loginRequest.getPassword(), passwordEncoder);
		AuthPrincipal authPrincipal = AuthPrincipal.from(member);

		String accessToken = tokenProvider.createToken(principalToJson(authPrincipal));
		return LoginResponse.of(accessToken, member.getName(), member.getRole(), member.getEmail());
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
