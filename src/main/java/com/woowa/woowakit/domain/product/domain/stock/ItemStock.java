package com.woowa.woowakit.domain.product.domain.stock;

import com.woowa.woowakit.domain.model.Quantity;
import lombok.Getter;

@Getter
public class ItemStock {

    private final Long stockId;
    private final Quantity quantity;

    public ItemStock(final Long stockId, final Quantity quantity) {
        this.stockId = stockId;
        this.quantity = quantity;
    }
}
