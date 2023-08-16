package com.woowa.woowakit.domain.product.domain.stock;

import lombok.Getter;

@Getter
public class StockQuantity {

	private final long quantity;

	private StockQuantity(final long quantity) {
		this.quantity = quantity;
	}

	public static StockQuantity from(final long quantity) {
		return new StockQuantity(quantity);
	}
}
