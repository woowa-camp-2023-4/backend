package com.woowa.woowakit.domain.order.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.order.domain.converter.TotalPriceConverter;

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

	@Convert(converter = TotalPriceConverter.class)
	private TotalPrice totalPrice;

	private Long memberId;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
	@OrderColumn(name = "item_index")
	private List<OrderItem> orderItems;

}
