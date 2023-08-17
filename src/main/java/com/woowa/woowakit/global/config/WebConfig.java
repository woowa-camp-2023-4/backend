package com.woowa.woowakit.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.woowa.woowakit.global.argument.AuthPrincipalResolver;
import com.woowa.woowakit.global.interceptor.AdminAuthorityInterceptor;
import com.woowa.woowakit.global.interceptor.AuthenticationInterceptor;
import com.woowa.woowakit.global.interceptor.UserAuthorityInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthenticationInterceptor authenticationInterceptor;
	private final AdminAuthorityInterceptor adminAuthorityInterceptor;
	private final UserAuthorityInterceptor userAuthorityInterceptor;
	private final AuthPrincipalResolver authPrincipalResolver;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry
			.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/**")
			.order(0);

		registry
			.addInterceptor(userAuthorityInterceptor)
			.addPathPatterns("/**")
			.order(1);

		registry
			.addInterceptor(adminAuthorityInterceptor)
			.addPathPatterns("/**")
			.order(2);
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authPrincipalResolver);
	}
}
