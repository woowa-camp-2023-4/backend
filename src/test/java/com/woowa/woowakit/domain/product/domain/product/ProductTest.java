package com.woowa.woowakit.domain.product.domain.product;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.exception.UpdateProductStatusFailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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

    @Test
    @DisplayName("Product 가 IN_STOCK 이라면 구매가능한 상태이므로 true 를 반환한다.")
    void isAvailablePurchaseTest() {
        // given
        Product product = Product.builder()
                .status(ProductStatus.IN_STOCK)
                .quantity(Quantity.from(100))
                .build();

        // when , then
        assertThat(product.isAvailablePurchase()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = ProductStatus.class, names = {"IN_STOCK"}, mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("Product 가 IN_STOCK 가 아니라면 구매가능한 상태이므로 false 를 반환한다.")
    void isNotAvailablePurchaseTest(final ProductStatus productStatus) {
        // given
        Product product = Product.builder()
                .status(productStatus)
                .quantity(Quantity.from(100))
                .build();

        // when , then
        assertThat(product.isAvailablePurchase()).isFalse();
    }

    @Test
    @DisplayName("Product 상태를 변경할 수 있다.")
    void updateProductStatus() {
        // given
        Product product = Product.builder()
                .status(ProductStatus.PRE_REGISTRATION)
                .quantity(Quantity.from(100))
                .build();

        // when
        product.updateProductStatus(ProductStatus.IN_STOCK);

        // then
        assertThat(product.getStatus()).isEqualTo(ProductStatus.IN_STOCK);
    }

    @Test
    @DisplayName("재고가 0이라면 Product 상태를 판매 중 상태로 변경할 수 없다")
    void updateProductStatusFail() {
        // given
        Product product = Product.builder()
                .status(ProductStatus.PRE_REGISTRATION)
                .quantity(Quantity.from(0))
                .build();

        // when , then
        assertThatCode(() -> product.updateProductStatus(ProductStatus.IN_STOCK))
                .isInstanceOf(UpdateProductStatusFailException.class)
                .hasMessage("재고가 0인 상태는 판매 중 상태로 변경할 수 없습니다.");
    }
}
