package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentHandler {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @Transactional
    @EventListener
    public void handle(final OrderCompleteEvent event) {
        Order order = event.getOrder();
        paymentService.validatePayment(
            event.getPaymentKey(),
            order.getUuid(),
            order.getTotalPrice());
        Payment payment = Payment.of(event.getPaymentKey(), order.getTotalPrice(), order.getUuid(),
            order.getId());

        paymentRepository.save(payment);
    }
}
