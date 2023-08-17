package com.woowa.woowakit.domain.payment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYMENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderId;

	private String paymentKey;

	public Payment(final Long orderId, final String paymentKey) {
		this.orderId = orderId;
		this.paymentKey = paymentKey;
		super.registerEvent(new PaymentSuccessEvent(this));
	}

	public static Payment of(final Long orderId, final String paymentKey) {
		return new Payment(orderId, paymentKey);
	}
}
