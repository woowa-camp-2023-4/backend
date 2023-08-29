package com.woowa.woowakit.domain.payment.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.converter.MoneyConverter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "payments")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "payment_key")
	private String paymentKey;

	@Convert(converter = MoneyConverter.class)
	@Column(name = "total_price")
	private Money totalPrice;

	@Builder
	private Payment(
		final String paymentKey,
		final Money totalPrice,
		final Long orderId
	) {
		this.paymentKey = paymentKey;
		this.totalPrice = totalPrice;
		this.orderId = orderId;
	}

	public static Payment of(
		final String paymentKey,
		final Money totalPrice,
		final Long orderId
	) {
		return Payment.builder()
			.paymentKey(paymentKey)
			.totalPrice(totalPrice)
			.orderId(orderId)
			.build();
	}
}
