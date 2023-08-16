package com.woowa.woowakit.domain.product.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
		assertThat(product).extracting("quantity").isEqualTo(0L);
	}
}