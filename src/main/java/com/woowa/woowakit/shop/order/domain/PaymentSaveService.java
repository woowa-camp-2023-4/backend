package com.woowa.woowakit.shop.order.domain;

import com.woowa.woowakit.shop.model.Money;

public interface PaymentSaveService {

	void save(Long orderId, Money totalPrice, String paymentKey);
}
