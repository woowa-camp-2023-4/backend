package com.woowa.woowakit.domain.product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.stock.ExpiryDate;
import com.woowa.woowakit.domain.product.domain.stock.Stock;
import com.woowa.woowakit.domain.product.domain.stock.StockRepository;
import com.woowa.woowakit.domain.product.dto.request.StockCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;
	private final ProductRepository productRepository;

	@Transactional
	public long create(final StockCreateRequest request, final Long productId) {
		final Product product = productRepository.findById(productId)
			.orElseThrow();

		final Stock stock = stockRepository
			.findByProductIdAndExpiryDate(productId, ExpiryDate.from(request.getExpiryDate()))
			.orElse(Stock.of(request.getExpiryDate(), product));

		stock.addQuantity(Quantity.from(request.getQuantity()));

		return stockRepository.save(stock).getId();
	}
}
