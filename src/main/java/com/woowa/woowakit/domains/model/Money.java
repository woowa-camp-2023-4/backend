package com.woowa.woowakit.domains.model;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money)) {
            return false;
        }
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Money{" +
            "value=" + value +
            '}';
    }
}
