package com.woowa.woowakit.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Money 단위 테스트")
class MoneyTest {

    @Test
    @DisplayName("머니에 숫자를 곱한다")
    void multiply() {
        Money money = Money.from(1000L);
        Money result = money.multiply(2L);
        Assertions.assertThat(result).isEqualTo(Money.from(2000L));
    }

    @Test
    @DisplayName("머니에 머니를 더한다")
    void add() {
        Money money = Money.from(1000L);
        Money result = money.add(Money.from(2000L));
        Assertions.assertThat(result).isEqualTo(Money.from(3000L));
    }
}
