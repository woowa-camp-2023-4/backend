package com.woowa.woowakit.domain.payment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.payment.domain.Payment;
import com.woowa.woowakit.domain.payment.domain.PaymentRepository;
import com.woowa.woowakit.domain.payment.dto.request.PaymentSuccessRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	@Transactional
	public void create(final PaymentSuccessRequest request) {
		// TODO: 외부 API 조회하여 검증

		paymentRepository.save(Payment.of(request.getOrderId(), request.getPaymentKey()));
	}
}
