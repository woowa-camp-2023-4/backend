package com.woowa.woowakit.shop.product.domain.product;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.woowa.woowakit.shop.product.exception.ProductQuantityNegativeException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SaleQuantity {

	@Column(name = "sale")
	private long value;

	private SaleQuantity(final long value) {
		validQuantity(value);
		this.value = value;
	}

	public static SaleQuantity from(final long saleQuantity) {
		return new SaleQuantity(saleQuantity);
	}

	private void validQuantity(final long value) {
		if (value < 0) {
			throw new ProductQuantityNegativeException();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SaleQuantity that = (SaleQuantity)o;
		return value == that.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
