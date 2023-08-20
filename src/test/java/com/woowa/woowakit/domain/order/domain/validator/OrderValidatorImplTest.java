package com.woowa.woowakit.domain.order.domain.validator;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.exception.NotMyOrderException;
import com.woowa.woowakit.domain.order.exception.QuantityNotEnoughException;
import com.woowa.woowakit.domain.order.fixture.OrderFixture;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("OrderValidatorImpl 단위테스트")
class OrderValidatorImplTest {

    private OrderValidatorImpl orderValidatorImpl;

    @BeforeEach
    public void setUp() {
        orderValidatorImpl = new OrderValidatorImpl(Mockito.mock(ProductRepository.class));
    }

    @Test
    @DisplayName("주문의 유저가 요청의 유저와 다르면 예외를 반환한다")
    void test1() {
        // given
        Long requestMemberId = 1L;
        Order order = OrderFixture.anOrder()
            .memberId(2L).build();

        // when then
        Assertions.assertThatThrownBy(() -> orderValidatorImpl.validate(requestMemberId, order))
            .isInstanceOf(NotMyOrderException.class);
    }

    @Test
    @DisplayName("주문한 상품의 양보다 존재하는 양이 적은 경우 예외를 반환한다")
    void test2() {
        // given
        OrderItem orderItem = OrderFixture.anOrderItem()
            .quantity(Quantity.from(4L)).build();

        // when then
        Assertions.assertThatThrownBy(
                () -> orderValidatorImpl.validateEnoughProductQuantity(
                    orderItem,
                    Quantity.from(3L)
                ))
            .isInstanceOf(QuantityNotEnoughException.class);
    }
}
