package com.woowa.woowakit.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class BatchAsyncConfig {

	@Bean(name = "StockProcessingService", destroyMethod = "shutdown")
	public ThreadPoolTaskExecutor healthCheckRequesterTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(Integer.MAX_VALUE);
		return executor;
	}
}
