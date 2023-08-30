package com.woowa.woowakit.domain.product.application;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.product.InStockProductSearchCondition;

@SpringBootTest
@DisplayName("Product 목록 조회 캐시 적용 테스트")
class ProductCacheTest {

	@SpyBean
	private ProductService productService;

	@MockBean
	private ProductRepository productRepository;

	@Test
	@DisplayName("메인 페이지 상품을 5초 내로 다시 조회하면 캐시에서 값을 꺼내서 보여준다.")
	void useCache() {
		InStockProductSearchCondition inStockProductSearchCondition = InStockProductSearchCondition.builder().build();

		when(productRepository.searchInStockProducts(inStockProductSearchCondition)).thenReturn(List.of());

		productService.findRankingProducts();
		productService.findRankingProducts();
		productService.findRankingProducts();

		verify(productRepository, times(1)).searchInStockProducts(inStockProductSearchCondition);
	}
}
