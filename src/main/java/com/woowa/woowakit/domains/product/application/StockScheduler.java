package com.woowa.woowakit.domains.product.application;

import com.woowa.woowakit.domains.product.domain.product.ProductRepository;
import com.woowa.woowakit.domains.product.domain.stock.Stock;
import com.woowa.woowakit.domains.product.domain.stock.StockRepository;
import com.woowa.woowakit.domains.product.domain.stock.StockType;
import com.woowa.woowakit.domains.product.exception.StockBatchFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockScheduler {

	private final ProductRepository productRepository;
	private final StockRepository stockRepository;
	private final StockProcessingService stockProcessingService;

	@Scheduled(zone = "Asia/Seoul", cron = "0 0 0 * * ?")
	public void trigger() {
		long startEnd = System.currentTimeMillis();
		log.info("재고 정합성 스케쥴러 시작 = {}", LocalDateTime.now());
		List<Long> productIds = productRepository.findAllIds();
		for (Long productId : productIds) {
			List<Stock> stocks = stockRepository.findAllByProductId(productId, StockType.NORMAL);
			doStockProcess(productId, stocks);
		}
		long diffTime = System.currentTimeMillis() - startEnd;
		log.info("재고 정합성 스케쥴러 끝 = {} , 걸린 시간 = {} ms ", LocalDateTime.now(), diffTime);
	}

	private void doStockProcess(final Long productId, final List<Stock> stocks) {
		try {
			stockProcessingService.doAsyncStockProcess(productId, LocalDate.now(ZoneId.of("Asia/Seoul")), stocks);
		} catch (Exception e) {
			log.warn("productId = {} 배치 처리 실패", productId);
			throw new StockBatchFailException(e);
		}
	}
}
