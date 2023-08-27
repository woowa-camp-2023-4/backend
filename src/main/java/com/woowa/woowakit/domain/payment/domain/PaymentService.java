package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.model.Money;
import reactor.core.publisher.Mono;

public interface PaymentService {

	Mono<Void> validatePayment(String paymentKey, String orderToken, Money totalPrice);
}
