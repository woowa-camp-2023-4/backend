package com.woowa.woowakit.infra.payment.toss;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThreadSleepClient 테스트")
class ThreadSleepClientTest {

	@Test
	@DisplayName("결제 지연시간 테스트")
	void test() {
		long start = System.currentTimeMillis();
		ThreadSleepClient threadSleepClient = new ThreadSleepClient();
		threadSleepClient.validatePayment("paymentKey", "orderToken", null);
		Assertions.assertThat(System.currentTimeMillis() - start).isGreaterThan(500);
	}
}
