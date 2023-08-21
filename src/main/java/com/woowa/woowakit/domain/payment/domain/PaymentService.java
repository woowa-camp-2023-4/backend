package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.model.Money;

public interface PaymentService {

	void validatePayment(String paymentKey, String orderToken, Money totalPrice);
}
