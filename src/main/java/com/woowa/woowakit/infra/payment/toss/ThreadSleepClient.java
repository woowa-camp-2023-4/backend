package com.woowa.woowakit.infra.payment.toss;

import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.payment.domain.PaymentService;
import java.time.Duration;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ThreadSleepClient implements PaymentService {

	private static final double LATENCY_MIN = 1.3;
	private static final double STANDARD_DEVIATION = 0.2;

	@Override
	public Mono<Void> validatePayment(
		final String paymentKey,
		final String orderToken,
		final Money totalPrice
	) {
		long latancyMs =
			(long) ((LATENCY_MIN + STANDARD_DEVIATION * new Random().nextGaussian()) * 1000);
		log.info("결제 요청 반환에 {} ms 가 수행됩니다. paymentKey: {}", latancyMs, paymentKey);

		return Mono.delay(Duration.ofMillis(latancyMs))
			.log("[mono]mono delay에 " + latancyMs + " ms가 수행되었습니다. paymentKey: " + paymentKey)
			.then();
	}
}
