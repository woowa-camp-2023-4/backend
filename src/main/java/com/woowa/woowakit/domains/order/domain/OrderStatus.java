package com.woowa.woowakit.domains.order.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
	ORDERED, PLACED, CANCELED, PAYED,
}
