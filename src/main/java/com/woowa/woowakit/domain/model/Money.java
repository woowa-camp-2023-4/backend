package com.woowa.woowakit.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Money {

	private final Long value;

	public static Money from(final Long value) {
		return new Money(value);
	}
}
