package com.woowa.woowakit.domain.product.domain;

import com.woowa.woowakit.domain.product.exception.ProductQuantityNegativeException;

import lombok.Getter;

@Getter
public class ProductQuantity {

	private final long quantity;

	private ProductQuantity(final long quantity) {
		validNotNegative(quantity);
		this.quantity = quantity;
	}

	public static ProductQuantity from(final long quantity) {
		return new ProductQuantity(quantity);
	}

	private void validNotNegative(final long quantity) {
		if (quantity < 0) {
			throw new ProductQuantityNegativeException();
		}
	}
}
