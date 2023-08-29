package com.woowa.woowakit.domain.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.converter.MoneyConverter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Table(name = "orders")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private final List<OrderItem> orderItems = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus orderStatus;

	@Convert(converter = MoneyConverter.class)
	@Column(name = "total_price")
	private Money totalPrice;

	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "uuid")
	private String uuid;

	@Builder
	private Order(final Long memberId, final List<OrderItem> orderItems) {
		this.orderStatus = OrderStatus.ORDERED;
		this.totalPrice = calculateTotalPrice(orderItems);
		this.memberId = memberId;
		this.orderItems.addAll(orderItems);
		this.uuid = UUID.randomUUID().toString();
	}

	public static Order of(
		final Long memberId,
		final List<OrderItem> orderItems
	) {
		return new Order(memberId, orderItems);
	}

	private Money calculateTotalPrice(final List<OrderItem> orderItems) {
		return orderItems.stream()
			.map(OrderItem::calculateTotalPrice)
			.reduce(Money.ZERO, Money::add);
	}

	public void place() {
		orderStatus = OrderStatus.PLACED;
	}

	public void cancel() {
		orderStatus = OrderStatus.CANCELED;
	}

	public void pay() {
		orderStatus = OrderStatus.PAYED;
	}

	public List<Long> collectProductIds() {
		return orderItems.stream()
			.map(OrderItem::getProductId)
			.collect(Collectors.toUnmodifiableList());
	}
}
