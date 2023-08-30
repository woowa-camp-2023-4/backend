package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.Money;

import reactor.core.publisher.Mono;

public interface PaymentClient {

	Mono<Void> validatePayment(String paymentKey, String orderToken, Money totalPrice);
}
