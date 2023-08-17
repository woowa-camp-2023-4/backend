package com.woowa.woowakit.domain.payment.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.woowakit.domain.payment.application.PaymentService;
import com.woowa.woowakit.domain.payment.dto.request.PaymentSuccessRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/payments/success")
	public ResponseEntity<Void> payment(@Valid @RequestBody final PaymentSuccessRequest request) {
		paymentService.create(request);
		return ResponseEntity.ok().build();
	}
}
