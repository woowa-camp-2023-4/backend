package com.woowa.woowakit.domain.product.application;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.exception.ProductNotExistException;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.stock.ExpirationDateProcessor;
import com.woowa.woowakit.domain.product.domain.stock.StockConsistencyProcessor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockProcessingService {

	private final ProductRepository productRepository;
	private final StockConsistencyProcessor stockConsistencyProcessor;
	private final ExpirationDateProcessor expirationDateProcessor;

	@Transactional
	public void doProcess(final Long productId, final LocalDate currentDate) {
		Product product = getProductById(productId);
		stockConsistencyProcessor.doProcess(product);
		expirationDateProcessor.doProcess(productId, currentDate);
	}

	private Product getProductById(final Long productId) {
		return productRepository.findByIdWithPessimistic(productId)
			.orElseThrow(ProductNotExistException::new);
	}
}
