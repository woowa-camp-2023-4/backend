package com.woowa.woowakit.domain.product.domain.stock;

import com.woowa.woowakit.domain.model.Quantity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Stocks {

    private final List<Stock> values;

    public Stocks(final List<Stock> values) {
        this.values = values.stream()
            .sorted(Comparator.comparing(o -> o.getExpiryDate().getDate()))
            .collect(Collectors.toList());
    }

    public List<ItemStock> bringOutStocks(Quantity quantity) {
        List<ItemStock> itemStocks = new ArrayList<>();

        for (Stock stock : values) {
            if (quantity.smallerThanOrEqualTo(stock.getQuantity())) {
                itemStocks.add(bringOut(stock, quantity));
                break;
            }

            quantity = quantity.subtract(stock.getQuantity());
            itemStocks.add(bringOut(stock, stock.getQuantity()));
        }

        return itemStocks;
    }

    private ItemStock bringOut(Stock stock, Quantity quantity) {
        stock.subtractQuantity(quantity);
        return new ItemStock(stock.getId(), quantity);
    }

    public List<Stock> getValues() {
        return Collections.unmodifiableList(values);
    }
}
