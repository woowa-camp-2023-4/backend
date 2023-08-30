package com.woowa.woowakit.global.interceptor;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.woowa.woowakit.domain.auth.annotation.User;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.global.error.UnAuthorizationException;

@Component
public class UserAuthorityInterceptor implements HandlerInterceptor {

	private static final String MEMBER_KEY = "memberId";

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		User user = handlerMethod.getMethodAnnotation(User.class);
		if (!Objects.isNull(user)) {
			validateUserAuthorization(request);
		}

		return true;
	}

	private void validateUserAuthorization(final HttpServletRequest request) {
		AuthPrincipal authPrincipal = (AuthPrincipal)request.getAttribute(MEMBER_KEY);
		if (Objects.isNull(authPrincipal)) {
			throw new UnAuthorizationException();
		}
	}
}
