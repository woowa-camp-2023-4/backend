package com.woowa.woowakit.global.argument;


import com.woowa.woowakit.domain.auth.annotation.Admin;
import com.woowa.woowakit.domain.auth.annotation.User;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.global.error.ForbiddenException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticatedResolver implements HandlerMethodArgumentResolver {

    private static final String MEMBER_KEY = "memberId";

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthPrincipal.class) &&
                (parameter.hasParameterAnnotation(User.class) || parameter.hasParameterAnnotation(Admin.class));
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        AuthPrincipal authPrincipal = getAuthPrincipal(webRequest);
        if (parameter.hasParameterAnnotation(Admin.class) && !authPrincipal.isAdmin()) {
            throw new ForbiddenException();
        }
        return authPrincipal;
    }

    private AuthPrincipal getAuthPrincipal(final NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest == null) {
            throw new IllegalStateException();
        }
        if (httpServletRequest.getAttribute(MEMBER_KEY) == null) {
            throw new IllegalStateException();
        }
        return (AuthPrincipal) httpServletRequest.getAttribute(MEMBER_KEY);
    }
}
