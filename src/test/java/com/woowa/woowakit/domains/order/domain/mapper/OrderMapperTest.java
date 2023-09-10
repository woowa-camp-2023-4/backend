package com.woowa.woowakit.domains.order.domain.mapper;


import com.woowa.woowakit.domains.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domains.order.exception.ProductNotFoundException;
import com.woowa.woowakit.domains.product.domain.product.ProductRepository;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@DisplayName("OrderMapper 단위 테스트")
@DataJpaTest
@Import({QuerydslTestConfig.class, JpaConfig.class, OrderMapper.class})
class OrderMapperTest {

	@MockBean
	ProductRepository productRepository;
	@Autowired
	OrderMapper orderMapper;

	@Test
	@DisplayName("존재하지 않는 product id를 입력하면 예외를 던진다")
	void throwExceptionForNotExistProductId() {
		// given
		Long productId = 1L;
		Mockito.when(productRepository.findAllById(List.of(productId))).thenReturn(List.of());

		// when
		Assertions.assertThatThrownBy(
				() -> orderMapper.mapFrom(1L, List.of(OrderCreateRequest.of(productId, 1L))))
			.isInstanceOf(ProductNotFoundException.class);
	}
}
