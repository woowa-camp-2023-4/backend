package com.woowa.woowakit.domain.product.domain;

import javax.persistence.Convert;

import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.MoneyConverter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductPrice {

	@Convert(converter = MoneyConverter.class)
	private Money price;

	public static ProductPrice of(final Money price) {
		return new ProductPrice(price);
	}

}
