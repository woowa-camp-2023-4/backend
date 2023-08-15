package com.woowa.woowakit.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Money {

	private Long value;

	public static Money of(final Long value) {
		return new Money(value);
	}
}
