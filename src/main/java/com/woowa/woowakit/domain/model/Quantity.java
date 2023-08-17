package com.woowa.woowakit.domain.model;

import com.woowa.woowakit.domain.product.exception.ProductQuantityNegativeException;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Quantity {

    private final long quantity;

    private Quantity(final long quantity) {
        validNotNegative(quantity);
        this.quantity = quantity;
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
        return Quantity.from(quantity + other.quantity);
    }

    public Quantity subtract(final Quantity other) {
        return Quantity.from(quantity - other.quantity);
    }

    public boolean smallerThanOrEqualTo(final Quantity other) {
        return quantity <= other.quantity;
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
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    @Override
    public String toString() {
        return "Quantity{" +
            "quantity=" + quantity +
            '}';
    }
}
