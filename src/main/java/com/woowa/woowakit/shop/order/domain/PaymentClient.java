package com.woowa.woowakit.shop.order.domain;

import com.woowa.woowakit.shop.model.Money;

import reactor.core.publisher.Mono;

public interface PaymentClient {

	Mono<Void> validatePayment(String paymentKey, String orderToken, Money totalPrice);
}
