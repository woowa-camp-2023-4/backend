package com.woowa.woowakit.domain.product.domain.stock;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Stocks 테스트")
class StocksTest {

    @Test
    @DisplayName("stocks는 생성할 떄 유통기한 순으로 정렬된다")
    void createOrderByExpiry() {
        // given
        Stock stock1 = Stock.of(LocalDate.now().plusDays(300), null);
        Stock stock2 = Stock.of(LocalDate.now().plusDays(200), null);
        Stock stock3 = Stock.of(LocalDate.now().plusDays(100), null);

        List<Stock> stocks = List.of(stock1, stock2, stock3);

        // when
        Stocks result = new Stocks(stocks);

        // then
        Assertions.assertThat(result.getValues()).containsExactly(stock3, stock2, stock1);
    }

    @Test
    @DisplayName("원하는 만큼의 재고를 꺼내고 기존 재고의 수량은 줄어들고 product의 총량도 그만큼 줄어든다")
    void bringOutStocks() {
        // given
        Product product = Product.of("test", 10000L, "test.jpg");

        Stock stock1 = Stock.of(LocalDate.now().plusDays(100), product);
        stock1.addQuantity(Quantity.from(3));
        Stock stock2 = Stock.of(LocalDate.now().plusDays(200), product);
        stock2.addQuantity(Quantity.from(5));
        Stock stock3 = Stock.of(LocalDate.now().plusDays(300), product);
        stock3.addQuantity(Quantity.from(10));

        Stocks stocks = new Stocks(List.of(stock1, stock2, stock3));

        // when
        List<ItemStock> itemStocks = stocks.bringOutStocks(Quantity.from(10));

        // then
        Assertions.assertThat(itemStocks.get(0))
            .extracting(ItemStock::getStockId, ItemStock::getQuantity)
            .containsExactly(stock1.getId(), Quantity.from(3));

        Assertions.assertThat(itemStocks.get(1))
            .extracting(ItemStock::getStockId, ItemStock::getQuantity)
            .containsExactly(stock2.getId(), Quantity.from(5));

        Assertions.assertThat(itemStocks.get(2))
            .extracting(ItemStock::getStockId, ItemStock::getQuantity)
            .containsExactly(stock3.getId(), Quantity.from(2));

        Assertions.assertThat(product.getQuantity()).isEqualTo(Quantity.from(8));
    }

}
