package com.woowa.woowakit.domain.order.application;

import java.util.List;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.domain.validator.OrderValidator;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.order.exception.OrderNotFoundException;
import com.woowa.woowakit.domain.order.exception.ProductNotFoundException;
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
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderByOrderIdAndMemberId(final AuthPrincipal authPrincipal, final Long orderId) {
        Order order = getOrderByOrderIdAndMemberId(authPrincipal, orderId);
        return OrderDetailResponse.from(order);
    }

    private Order getOrderByOrderIdAndMemberId(final AuthPrincipal authPrincipal, final Long orderId) {
        return orderRepository.findOrderById(orderId, authPrincipal.getId())
            .orElseThrow(OrderNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResponse> findAllOrderByMemberId(final AuthPrincipal authPrincipal) {
        List<Order> orders = orderRepository.findAllByMemberId(authPrincipal.getId());
        return OrderDetailResponse.listOf(orders);
    }

    @Transactional
    public PreOrderResponse preOrder(final AuthPrincipal authPrincipal, final PreOrderCreateRequest request) {
        Product product = getProductById(request.getProductId());
        Order order = orderMapper.mapFrom(authPrincipal.getId(), product, request.getQuantity());
        return PreOrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public Long order(final AuthPrincipal authPrincipal, final OrderCreateRequest request) {
        Order order = getOrderById(request.getOrderId());
        order.order(authPrincipal.getId(), orderValidator);
        takeStockOut(order);
        paymentService.validatePayment(
            request.getPaymentKey(),
            order.getUuid(),
            order.getTotalPrice()
        );
        return order.getId();
    }

    private Order getOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(OrderNotFoundException::new);
    }

    private Product getProductById(final Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
    }

    private void takeStockOut(final Order order) {
        order.getOrderItems().forEach(this::takeStockOut);
    }

    private void takeStockOut(final OrderItem orderItem) {
        Stocks stocks = new Stocks(stockRepository.findAllByProductId(orderItem.getProductId()));
        orderItem.addOrderItemStocks(stocks.bringOutStocks(orderItem.getQuantity()));
    }
}
