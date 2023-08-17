package com.woowa.woowakit.domain.order.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;

import lombok.Getter;

@Getter
@Embeddable
public class OrderItem {
	private Long productId;
	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private String image;

	@Column(name = "price")
	private int price;

	@Convert(converter = QuantityConverter.class)
	private Quantity quantity;

	protected OrderItem() {

	}

}
