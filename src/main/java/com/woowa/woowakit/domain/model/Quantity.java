package com.woowa.woowakit.domain.model;

import com.woowa.woowakit.domain.product.exception.ProductQuantityNegativeException;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Quantity {

    private final long value;

    private Quantity(final long value) {
        validNotNegative(value);
        this.value = value;
    }

    public static Quantity from(final long quantity) {
        return new Quantity(quantity);
    }

    private void validNotNegative(final long quantity) {
        if (quantity < 0) {
            throw new ProductQuantityNegativeException();
        }
    }

    public Quantity add(final Quantity other) {
        return Quantity.from(value + other.value);
    }

    public Quantity subtract(final Quantity other) {
        return Quantity.from(value - other.value);
    }

    public boolean smallerThan(final Quantity other) {
        return value < other.value;
    }

    public boolean smallerThanOrEqualTo(final Quantity other) {
        return value <= other.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity that = (Quantity) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Quantity{" +
            "value=" + value +
            '}';
    }
}
