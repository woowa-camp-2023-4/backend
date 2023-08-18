package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.converter.MoneyConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "orders")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // @Embedded
    // private Payment payment;

    @Convert(converter = MoneyConverter.class)
    private Money totalPrice;

    private Long memberId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private final List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "uuid")
    private String uuid;

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
}
