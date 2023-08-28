package com.woowa.woowakit.domain.order.domain;

public interface OrderPayService {

	void pay(Long orderId, String paymentKey);
}
