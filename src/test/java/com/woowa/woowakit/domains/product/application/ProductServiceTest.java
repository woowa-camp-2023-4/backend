package com.woowa.woowakit.domains.product.application;

import com.woowa.woowakit.domains.product.exception.ProductNotExistException;
import com.woowa.woowakit.global.config.JpaConfig;
import com.woowa.woowakit.global.config.QuerydslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Import({ProductService.class, QuerydslTestConfig.class, JpaConfig.class})
@DisplayName("ProductService 테스트")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("존재하지 않은 id로 Product를 조회하면 예외가 발생한다.")
    void findExceptionIfIdNotExists() {
        // given
        long notExistId = 9999L;

        // when, then
        assertThatCode(() -> productService.findById(notExistId))
                .isInstanceOf(ProductNotExistException.class)
                .hasMessage("존재하지 않은 상품 정보입니다.");
    }
}
