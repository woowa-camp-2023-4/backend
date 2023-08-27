package com.woowa.woowakit.domain.product.application;

import static java.lang.System.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.exception.ProductNotExistException;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.stock.ExpirationDateProcessor;
import com.woowa.woowakit.domain.product.domain.stock.Stock;
import com.woowa.woowakit.domain.product.domain.stock.StockConsistencyProcessor;
import com.woowa.woowakit.domain.product.domain.stock.StockRepository;
import com.woowa.woowakit.domain.product.domain.stock.StockType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockProcessingService {

	public static final int DAYS = 6;
	public static final long DEFAULT_SUBTRACT = 0L;
	private final ProductRepository productRepository;
	private final StockConsistencyProcessor stockConsistencyProcessor;
	private final ExpirationDateProcessor expirationDateProcessor;
	private final StockRepository stockRepository;

	@Async("StockProcessingService")
	@Transactional
	public void doAsyncStockProcess(
		final Long productId,
		final LocalDate currentDate,
		final List<Stock> stocks
	) {
		long startTime = currentTimeMillis();
		doStockProcess(productId, currentDate, stocks);
		log.info("productId = {} 상품 사이클 걸린 시간 = {}ms ", productId, currentTimeMillis() - startTime);
	}

	@Transactional
	public void doStockProcess(final Long productId, final LocalDate currentDate, final List<Stock> stocks) {
		Product product = getProductById(productId);
		doStockConsistencyProcess(stocks, product);
		doExpirationDateProcess(productId, currentDate);
		subtractProductQuantity(productId, currentDate, product);
	}

	private Product getProductById(final Long productId) {
		return productRepository.findByIdWithPessimistic(productId)
			.orElseThrow(ProductNotExistException::new);
	}

	private void doStockConsistencyProcess(final List<Stock> stocks, final Product product) {
		long stockConsistencyProcessorStartTime = currentTimeMillis();
		stockConsistencyProcessor.run(product, stocks);
		log.info("stockConsistencyProcessor 걸린 시간 = {}ms ", currentTimeMillis() - stockConsistencyProcessorStartTime);
	}

	private void doExpirationDateProcess(final Long productId, final LocalDate currentDate) {
		long expirationDateProcessorStartTime = currentTimeMillis();
		expirationDateProcessor.run(productId, currentDate);
		log.info("expirationDateProcessor 걸린 시간 = {}ms ", currentTimeMillis() - expirationDateProcessorStartTime);
	}

	private void subtractProductQuantity(final Long productId, final LocalDate currentDate, final Product product) {
		long subtractExpiryQuantity = stockRepository.countStockByExpiry(productId, StockType.EXPIRED.name(),
				currentDate.plusDays(DAYS))
			.orElse(DEFAULT_SUBTRACT);
		log.info("유통기한 정책에 의해 차감된 재고 = {}", subtractExpiryQuantity);
		product.subtractQuantity(Quantity.from(subtractExpiryQuantity));
	}
}
