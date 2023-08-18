package com.woowa.woowakit.domain.order.application;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.exception.ProductNotFoundException;
import com.woowa.woowakit.domain.order.exception.QuantityNotEnoughException;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import com.woowa.woowakit.domain.product.domain.stock.StockRepository;
import com.woowa.woowakit.domain.product.domain.stock.Stocks;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    
    @Transactional
    public Long order(AuthPrincipal authPrincipal, OrderCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(ProductNotFoundException::new);

        Order order = orderMapper.mapFrom(authPrincipal.getId(), product, request.getQuantity());
        orderRepository.save(order);

        validateEnoughProductQuantity(request);
        takeStockOut(order);

        return order.getId();
    }

    private void validateEnoughProductQuantity(OrderCreateRequest request) {
        Quantity quantityCount = productRepository.findQuantityCountById(
                request.getProductId())
            .orElseThrow(ProductNotFoundException::new);

        if (quantityCount.smallerThan(Quantity.from(request.getQuantity()))) {
            throw new QuantityNotEnoughException();
        }
    }

    private void takeStockOut(Order order) {
        order.getOrderItems().forEach(this::takeStockOut);
    }

    private void takeStockOut(OrderItem orderItem) {
        Stocks stocks = new Stocks(stockRepository.findAllByProductId(orderItem.getProductId()));
        orderItem.addOrderItemStocks(stocks.bringOutStocks(orderItem.getQuantity()));
    }
}
