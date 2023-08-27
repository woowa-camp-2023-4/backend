package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.converter.MoneyConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Payment)) return false;
		final Payment payment = (Payment) o;
		return Objects.equals(id, payment.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
