package com.woowa.woowakit.domain.product.application;

import java.time.LocalDate;
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
		List<Long> productIds = productRepository.findIdAll();

		for (Long productId : productIds) {
			stockProcessingService.doProcess(productId, LocalDate.now(ZoneId.of("Asia/Seoul")));
		}
	}
}
