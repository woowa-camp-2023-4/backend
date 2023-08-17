package com.woowa.woowakit.domain.product.domain.stock;

import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.Quantity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemStock {

    private final Long stockId;
    private final Quantity quantity;
    private final Long productId;
    private final String productName;
    private final Image productImage;
    private final Money productPrice;

    @Builder
    private ItemStock(Long stockId, Quantity quantity, Long productId, String productName,
        Image productImage, Money productPrice) {
        this.stockId = stockId;
        this.quantity = quantity;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
    }

    public static ItemStock of(
        Long stockId,
        Quantity quantity,
        Long productId,
        String productName,
        Image productImage,
        Money productPrice
    ) {
        return ItemStock.builder()
            .stockId(stockId)
            .quantity(quantity)
            .productId(productId)
            .productName(productName)
            .productImage(productImage)
            .productPrice(productPrice)
            .build();
    }
}
