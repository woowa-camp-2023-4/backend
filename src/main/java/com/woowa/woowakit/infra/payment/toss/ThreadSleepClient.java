package com.woowa.woowakit.infra.payment.toss;

import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.payment.domain.PaymentService;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadSleepClient implements PaymentService {

	private static final double MEAN = 1.3;
	private static final double STANDARD_DEVIATION = 0.2;

	@Override
	public void validatePayment(String paymentKey, String orderToken, Money totalPrice) {
		try {
			long latancyMs =
				(long) ((MEAN + STANDARD_DEVIATION * new Random().nextGaussian()) * 1000);
			log.info("결제를 위해 {} ms 대기합니다.", latancyMs);
			Thread.sleep(latancyMs);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
