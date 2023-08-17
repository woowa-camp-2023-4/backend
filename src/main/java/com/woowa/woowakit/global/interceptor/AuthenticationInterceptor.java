package com.woowa.woowakit.global.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.auth.domain.MemberRepository;
import com.woowa.woowakit.domain.auth.infra.TokenProvider;
import com.woowa.woowakit.global.error.NotFoundMemberException;
import com.woowa.woowakit.global.error.TokenInvalidException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

	private static final String MEMBER_KEY = "memberId";
	private static final String BEARER_TYPE = "Bearer";

	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;
	private final ObjectMapper objectMapper;

	@Override
	public boolean preHandle(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Object handler
	) {
		if (isEmptyToken(request)) {
			return true;
		}

		String accessToken = getAccessToken(request);
		validateToken(accessToken);

		AuthPrincipal authPrincipal = getAuthPrincipal(accessToken);
		validateMember(authPrincipal.getId());
		request.setAttribute(MEMBER_KEY, authPrincipal);

		return true;
	}

	private boolean isEmptyToken(HttpServletRequest request) {
		return !StringUtils.hasText(request.getHeader(HttpHeaders.AUTHORIZATION));
	}

	private String getAccessToken(final HttpServletRequest request) {
		String value = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(value) || !value.startsWith(BEARER_TYPE)) {
			throw new TokenInvalidException();
		}

		return value.substring(BEARER_TYPE.length()).trim();
	}

	private void validateToken(final String accessToken) {
		if (!tokenProvider.validateToken(accessToken)) {
			throw new TokenInvalidException();
		}
	}

	private AuthPrincipal getAuthPrincipal(String accessToken) {
		try {
			return objectMapper.readValue(tokenProvider.getPayload(accessToken),
				AuthPrincipal.class);
		} catch (JsonProcessingException e) {
			log.error("AuthPrincipal 변환 중 에러가 발생했습니다. accessToken: {}", accessToken, e);
			throw new TokenInvalidException();
		}
	}

	private void validateMember(final Long memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw new NotFoundMemberException();
		}
	}
}
