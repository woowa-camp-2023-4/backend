package com.woowa.woowakit.domains.order.domain;

import com.woowa.woowakit.domains.model.Money;

import reactor.core.publisher.Mono;

public interface PaymentClient {

	Mono<Void> validatePayment(String paymentKey, String orderToken, Money totalPrice);
}
