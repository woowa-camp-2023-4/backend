package com.woowa.woowakit.domain.payment.domain;

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

@Table(name = "PAYMENTS")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderId;

	private String paymentKey;

	@Convert(converter = MoneyConverter.class)
	private Money totalPrice;

	private String uuid;

	@Builder
	private Payment(
		final String paymentKey,
		final Money totalPrice,
		final String uuid,
		final Long orderId
	) {
		this.paymentKey = paymentKey;
		this.totalPrice = totalPrice;
		this.uuid = uuid;
		this.orderId = orderId;
	}

	public static Payment of(
		final String paymentKey,
		final Money totalPrice,
		final String uuid,
		final Long orderId
	) {
		return Payment.builder()
			.paymentKey(paymentKey)
			.totalPrice(totalPrice)
			.uuid(uuid)
			.orderId(orderId)
			.build();
	}
}
