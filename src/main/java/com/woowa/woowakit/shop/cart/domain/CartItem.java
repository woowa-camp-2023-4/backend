package com.woowa.woowakit.shop.cart.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.woowa.woowakit.shop.model.BaseEntity;
import com.woowa.woowakit.shop.model.Quantity;
import com.woowa.woowakit.shop.model.converter.QuantityConverter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartItem extends BaseEntity {

	private static final Quantity INITIAL_QUANTITY = Quantity.from(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "product_id")
	private Long productId;

	@Convert(converter = QuantityConverter.class)
	private Quantity quantity;

	@Builder
	private CartItem(
		final Long memberId,
		final Long productId,
		final Quantity quantity
	) {
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

	public static CartItem of(
		final Long memberId,
		final Long productId
	) {
		return new CartItem(memberId, productId, INITIAL_QUANTITY);
	}

	public void addQuantity(final Quantity requiredQuantity, final CartItemValidator cartItemValidator) {
		quantity = quantity.add(requiredQuantity);
		cartItemValidator.validate(this);
	}

	public void updateQuantity(final long quantity, final CartItemValidator cartItemValidator) {
		this.quantity = Quantity.from(quantity);
		cartItemValidator.validate(this);
	}

	public boolean isMyCartItem(final Long memberId) {
		return Objects.equals(this.memberId, memberId);
	}
}
