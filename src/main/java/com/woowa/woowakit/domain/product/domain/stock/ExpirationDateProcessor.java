package com.woowa.woowakit.domain.product.domain.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpirationDateProcessor {

	private final StockRepository stockRepository;

	@Transactional
	public void run(final Long productId, final LocalDate currentDate) {
		stockRepository.updateStatus(StockType.EXPIRED, ExpiryDate.from(currentDate.plusDays(6)), productId);
	}
}
