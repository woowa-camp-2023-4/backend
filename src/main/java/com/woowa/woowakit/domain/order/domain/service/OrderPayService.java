package com.woowa.woowakit.domain.order.domain.service;

import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;

public interface OrderPayService {

	void pay(final OrderCompleteEvent event);
}
