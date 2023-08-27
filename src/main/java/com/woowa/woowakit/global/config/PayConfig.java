package com.woowa.woowakit.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.woowa.woowakit.domain.payment.domain.PaymentClient;
import com.woowa.woowakit.infra.payment.toss.ThreadSleepClient;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class PayConfig {

	@Profile("prod | local")
	@Bean
	public PaymentClient paymentClient() {
		return new ThreadSleepClient(monoDelay());
	}

	@Bean
	public Scheduler monoDelay() {
		return Schedulers.newParallel("mono-delay", 50);
	}
}
