package com.woowa.woowakit.domains.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowa.woowakit.domains.order.fixture.OrderFixture;

@DisplayName("Order 도메인 단위 테스트")
class OrderTest {

	@Test
	@DisplayName("Order가 롤백되면 orderStatus가 CANCELED로 변경된다.")
	void rollBackOrder() {
		// given
		Order order = OrderFixture.anOrder().build();

		// when
		order.cancel();

		// then
		assertThat(order).extracting("orderStatus").isEqualTo(OrderStatus.CANCELED);
	}
}
