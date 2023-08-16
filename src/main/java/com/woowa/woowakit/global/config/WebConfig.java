package com.woowa.woowakit.global.config;

import com.woowa.woowakit.global.argument.AuthenticatedResolver;
import com.woowa.woowakit.global.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthenticatedResolver authenticatedResolver;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // TODO : PATH 추가
        // registry.addInterceptor(authInterceptor);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedResolver);
    }
}
