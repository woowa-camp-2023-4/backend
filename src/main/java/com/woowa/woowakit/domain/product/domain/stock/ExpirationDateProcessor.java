package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpirationDateProcessor {

	private final StockRepository stockRepository;

	@Transactional
	public void doProcess(final Long productId, final LocalDate currentDate) {
		List<Stock> stocks = stockRepository.findAllByProductId(productId, StockType.NORMAL);
		for (Stock stock : stocks) {
			stock.CheckExpiredExpiryDate(currentDate);
		}
	}
}
