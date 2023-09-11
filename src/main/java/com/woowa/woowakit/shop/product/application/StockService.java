package com.woowa.woowakit.shop.product.application;

import com.woowa.woowakit.shop.product.domain.product.Product;
import com.woowa.woowakit.shop.product.domain.product.ProductRepository;
import com.woowa.woowakit.shop.product.domain.stock.ExpiryDate;
import com.woowa.woowakit.shop.product.domain.stock.Stock;
import com.woowa.woowakit.shop.product.domain.stock.StockRepository;
import com.woowa.woowakit.shop.product.exception.ProductNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.shop.model.Quantity;
import com.woowa.woowakit.shop.product.dto.request.StockCreateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;
	private final ProductRepository productRepository;

	@Transactional
	public long create(final StockCreateRequest request, final Long productId) {
		final Product product = findProductById(productId);

		final Stock stock = stockRepository
			.findByProductIdAndExpiryDate(productId, ExpiryDate.from(request.getExpiryDate()))
			.orElse(Stock.of(request.getExpiryDate(), product));

		log.info("StockService.create() 로직 실행 전: stockId = {} (새로 생성 시 null), quantity = {}, addQuantity = {}", stock.getId(), stock.getQuantity().getValue(), request.getQuantity());

		stock.addQuantity(Quantity.from(request.getQuantity()));

		log.info("StockService.create() 로직 실행 후: productId = {} (새로 생성 시 null), quantity = {}, addQuantity = {}", stock.getId(), stock.getQuantity().getValue(), request.getQuantity());

		return stockRepository.save(stock).getId();
	}

	private Product findProductById(final Long id) {
		return productRepository.findByIdWithPessimistic(id)
			.orElseThrow(ProductNotExistException::new);
	}
}
