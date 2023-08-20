package com.woowa.woowakit.domain.product.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Stock 도메인 단위 테스트")
class StockTest {

    @Test
    @DisplayName("재고를 추가하면 상품 재고 수량에도 반영된다.")
    void addQuantity() {
        // given
        Product product = Product.of("test", 100L, "/test.jpg");
        Stock stock = Stock.of(LocalDate.now().plusDays(1), product);

        // when
        stock.addQuantity(Quantity.from(5));

        // then
        assertThat(product).extracting(Product::getQuantity)
            .extracting(Quantity::getValue)
            .isEqualTo(5L);
    }
}
