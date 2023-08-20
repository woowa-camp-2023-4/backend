package com.woowa.woowakit.domain.product.domain.stock;

import com.woowa.woowakit.domain.model.Quantity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemStock {

    private final Long stockId;
    private final Quantity quantity;

    @Builder
    private ItemStock(final Long stockId, final Quantity quantity) {
        this.stockId = stockId;
        this.quantity = quantity;
    }

    public static ItemStock of(final Long stockId, final Quantity quantity) {
        return ItemStock.builder()
            .stockId(stockId)
            .quantity(quantity)
            .build();
    }
}
