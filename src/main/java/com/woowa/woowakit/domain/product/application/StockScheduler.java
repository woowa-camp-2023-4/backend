package com.woowa.woowakit.domain.product.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockScheduler {

	private final ProductRepository productRepository;
	private final StockProcessingService stockProcessingService;

	@Scheduled(zone = "Asia/Seoul", cron = "0 0 0 * * ?")
	public void trigger() {
		long startEnd = System.currentTimeMillis();
		log.info("재고 정합성 스케쥴러 시작 = {}", LocalDateTime.now());
		List<Long> productIds = productRepository.findAllIds();

		for (Long productId : productIds) {
			stockProcessingService.doProcess(productId, LocalDate.now(ZoneId.of("Asia/Seoul")));
		}
		long diffTime = System.currentTimeMillis() - startEnd;
		log.info("재고 정합성 스케쥴러 끝 = {} , 걸린 시간 = {} ms ", LocalDateTime.now(), diffTime);
	}
}
