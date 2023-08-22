package com.woowa.woowakit.domain.product.domain.stock;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockConsistencyProcessor {

	private final StockRepository stockRepository;

	@Transactional
	public void doProcess(final Product product) {
		List<Stock> stocks = stockRepository.findAllByProductId(product.getId(), StockType.NORMAL);
		Quantity differentQuantity = getTotalStockQuantity(stocks).subtract(product.getQuantity());

		for (Stock stock : stocks) {
			Quantity removalQuantity = computeRemovalQuantity(differentQuantity, stock);
			differentQuantity = differentQuantity.subtract(removalQuantity);
			stock.subtractQuantity(removalQuantity);
			removeStockIfEmpty(stock);
		}
	}

	private Quantity getTotalStockQuantity(final List<Stock> stocks) {
		return Quantity.from(stocks.stream()
			.mapToLong(stock -> stock.getQuantity().getValue())
			.sum());
	}

	private Quantity computeRemovalQuantity(final Quantity removingQuantity, final Stock stock) {
		return Quantity.from(Math.min(stock.getQuantity().getValue(), removingQuantity.getValue()));
	}

	private void removeStockIfEmpty(Stock stock) {
		if (stock.isEmpty()) {
			stockRepository.delete(stock);
		}
	}
}
