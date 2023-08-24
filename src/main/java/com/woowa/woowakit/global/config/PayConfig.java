package com.woowa.woowakit.global.config;

import com.woowa.woowakit.domain.payment.domain.PaymentService;
import com.woowa.woowakit.infra.payment.toss.ThreadSleepClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class PayConfig {

	@Profile("prod")
	@Bean
	public PaymentService paymentService() {
		return new ThreadSleepClient();
	}

}
