package com.woowa.woowakit.domain.order.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
	ORDERED, PLACED, CANCELED, PAYED,
}
