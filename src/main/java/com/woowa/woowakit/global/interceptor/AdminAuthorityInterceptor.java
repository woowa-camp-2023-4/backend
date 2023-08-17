package com.woowa.woowakit.global.interceptor;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.woowa.woowakit.domain.auth.annotation.Admin;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.global.error.ForbiddenException;

@Component
public class AdminAuthorityInterceptor implements HandlerInterceptor {

	private static final String MEMBER_KEY = "memberId";

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		Admin admin = handlerMethod.getMethodAnnotation(Admin.class);
		if (!Objects.isNull(admin)) {
			validateAdminAuthorization(request);
		}

		return true;
	}

	private void validateAdminAuthorization(final HttpServletRequest request) {
		AuthPrincipal authPrincipal = (AuthPrincipal)request.getAttribute(MEMBER_KEY);
		if (!authPrincipal.isAdmin()) {
			throw new ForbiddenException();
		}
	}
}
