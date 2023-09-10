package com.woowa.woowakit.domains.order.domain;

import com.woowa.woowakit.domains.model.Money;

public interface PaymentSaveService {

	void save(Long orderId, Money totalPrice, String paymentKey);
}
