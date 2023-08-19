package com.woowa.woowakit.domain.product.domain.product;

import com.woowa.woowakit.domain.model.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product 도메인 엔티티 테스트")
class ProductTest {

    @Test
    @DisplayName("Product 생성 시 가등록 상태로 생성된다.")
    void createProductWithPreRegistrationStatus() {
        // when
        Product product = Product.of("test name", 1000L, "test image url");

        // then
        assertThat(product).extracting("status").isEqualTo(ProductStatus.PRE_REGISTRATION);
    }

    @Test
    @DisplayName("Product 생성 시 수량이 0인 상태로 생성된다.")
    void createProductWithZeroQuantity() {
        // when
        Product product = Product.of("test name", 1000L, "test image url");

        // then
        assertThat(product).extracting("quantity").isEqualTo(Quantity.from(0));
    }

    @Test
    @DisplayName("Product 가 가진 수량보다 많은 수량을 요구할 경우 false 를 반환한다.")
    void isEnoughQuantityFalse() {
        // given
        Product product = Product.builder()
                .quantity(Quantity.from(100))
                .build();
        // when , then
        assertThat(product.isEnoughQuantity(Quantity.from(101))).isFalse();
    }

    @Test
    @DisplayName("Product 가 가진 수량보다 적거나 같은 수량을 요구할 경우 true 를 반환한다.")
    void isEnoughQuantityTrue() {
        // given
        Product product = Product.builder()
                .quantity(Quantity.from(100))
                .build();

        // when , then
        assertThat(product.isEnoughQuantity(Quantity.from(99))).isTrue();
    }
}
