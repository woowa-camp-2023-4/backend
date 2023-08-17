package com.woowa.woowakit.domain.product.domain.stock;

import com.woowa.woowakit.domain.product.exception.StockQuantityNegativeException;

import lombok.Getter;

@Getter
public class StockQuantity {

	private final long quantity;

	private StockQuantity(final long quantity) {
		validNotNegative(quantity);
		this.quantity = quantity;
	}

	public static StockQuantity from(final long quantity) {
		return new StockQuantity(quantity);
	}

	private void validNotNegative(final long quantity) {
		if (quantity < 0) {
			throw new StockQuantityNegativeException();
		}
	}
}
