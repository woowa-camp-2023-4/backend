package com.woowa.woowakit.global.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfig {

	@Bean
	CountedAspect countedAspect(MeterRegistry registry) {
		return new CountedAspect(registry);
	}
}
