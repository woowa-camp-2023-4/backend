package com.woowa.woowakit.infra.payment.toss;

import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.payment.domain.PaymentService;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadSleepClient implements PaymentService {

	public static final double MEAN = 1.3;
	public static final double STANDARD_DEVIATION = 0.2;
	private final Random random = new Random();

	@Override
	public void validatePayment(String paymentKey, String orderToken, Money totalPrice) {
		try {
			long latancyMs = (long) (random.nextGaussian(MEAN, STANDARD_DEVIATION) * 1000);
			log.info("결제를 위해 {} ms 대기합니다.", latancyMs);
			Thread.sleep(latancyMs);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
