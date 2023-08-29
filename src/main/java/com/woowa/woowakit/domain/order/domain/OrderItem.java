package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.ImageConverter;
import com.woowa.woowakit.domain.model.converter.MoneyConverter;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	@Convert(converter = ImageConverter.class)
	private Image image;

	@Column(name = "price")
	@Convert(converter = MoneyConverter.class)
	private Money price;

	@Convert(converter = QuantityConverter.class)
	private Quantity quantity;

	@Builder
	public OrderItem(
		final Long productId,
		final String name,
		final Image image,
		final Money price,
		final Quantity quantity
	) {
		this.productId = productId;
		this.name = name;
		this.image = image;
		this.price = price;
		this.quantity = quantity;
	}

	public static OrderItem of(
		final Long productId,
		final String name,
		final Image image,
		final Money price,
		final Quantity quantity
	) {
		return OrderItem.builder()
			.productId(productId)
			.name(name)
			.image(image)
			.price(price)
			.quantity(quantity)
			.build();
	}

	Money calculateTotalPrice() {
		return price.multiply(quantity.getValue());
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof OrderItem)) return false;
		final OrderItem orderItem = (OrderItem) o;
		return Objects.equals(id, orderItem.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
