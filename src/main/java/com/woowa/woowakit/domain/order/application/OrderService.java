package com.woowa.woowakit.domain.order.application;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.order.exception.OrderNotFoundException;
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

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    @Transactional
    public PreOrderResponse preOrder(AuthPrincipal authPrincipal, PreOrderCreateRequest request) {
        Product product = getProductById(request.getProductId());
        Order order = orderMapper.mapFrom(authPrincipal.getId(), product, request.getQuantity());
        return PreOrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public Long order(AuthPrincipal authPrincipal, OrderCreateRequest request) {
        Order order = getOrderById(request.getOrderId());
        order.validateSameUser(authPrincipal.getId());
        validateEnoughProductQuantity(order);
        takeStockOut(order);

        paymentService.validatePayment(
            request.getPaymentKey(),
            order.getUuid(),
            order.getTotalPrice()
        );
        
        return order.getId();
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(OrderNotFoundException::new);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
    }

    private void validateEnoughProductQuantity(Order order) {
        Quantity quantityCount = productRepository.findQuantityCountById(
                order.getOrderItems().get(0).getProductId())
            .orElseThrow(ProductNotFoundException::new);

        if (quantityCount.smallerThan(order.getOrderItems().get(0).getQuantity())) {
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
