package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.Money;

public interface PaymentSaveService {

	void save(Long orderId, Money totalPrice, String paymentKey);
}
