package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.converter.MoneyConverter;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

	public void order(final String paymentKey) {
		registerEvent(new OrderCompleteEvent(this, paymentKey));
		log.info("주문 완료 이벤트 발행 orderId: {}", id);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Order)) return false;
		final Order order = (Order) o;
		return Objects.equals(orderItems, order.orderItems);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderItems);
	}
}
