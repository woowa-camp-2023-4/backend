package com.woowa.woowakit.domain.cart.domain;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CART_ITEMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	private Long productId;

	@Convert(converter = QuantityConverter.class)
	private Quantity quantity;

	@Builder
	private CartItem(
		final Long memberId,
		final Long productId,
		final Quantity quantity) {
		this.memberId = memberId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static CartItem of(
		final Long memberId,
		final Long productId,
		final long quantity
	) {
		return new CartItem(memberId, productId, Quantity.from(quantity));
	}

	public void addQuantity(final Quantity quantity) {
		this.quantity = this.quantity.add(quantity);
	}
}
