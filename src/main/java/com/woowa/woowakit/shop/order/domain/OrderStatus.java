package com.woowa.woowakit.shop.order.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
	ORDERED, PLACED, CANCELED, PAYED,
}
