package com.woowa.woowakit.domain.product.domain.stock;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.ProductSalesRepository;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductSales;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockConsistencyProcessor {

	private final StockRepository stockRepository;
	private final ProductSalesRepository productSalesRepository;

	@Transactional
	public void run(final Product product) {
		List<Stock> stocks = stockRepository.findAllByProductId(product.getId(), StockType.NORMAL);
		Quantity difference = getTotalStockQuantity(stocks).subtract(product.getQuantity());
		saveProductSales(product.getId(), difference);

		for (Stock stock : stocks) {
			Quantity removalQuantity = computeRemovalQuantity(difference, stock);
			difference = difference.subtract(removalQuantity);
			stock.subtractQuantity(removalQuantity);
			removeStockIfEmpty(stock);
		}
	}

	private void saveProductSales(final Long productId, final Quantity quantity) {
		try {
			productSalesRepository.save(ProductSales.builder()
				.productId(productId)
				.saleDate(LocalDate.now().minusDays(1))
				.sale(Quantity.from(quantity.getValue()))
				.build());
		} catch (Exception e) {
			log.warn("일일 판매량을 저장하는데 실패했습니다.", e);
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

	private void removeStockIfEmpty(final Stock stock) {
		if (stock.isEmpty()) {
			stockRepository.delete(stock);
		}
	}
}
