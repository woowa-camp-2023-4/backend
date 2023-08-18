package com.woowa.woowakit.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Money {

    public static final Money ZERO = new Money(0L);

    private final Long value;

    public static Money from(final Long value) {
        return new Money(value);
    }

    public Money multiply(final Long value) {
        return new Money(this.value * value);
    }

    public Money add(final Money money) {
        return new Money(this.value + money.value);
    }
}
