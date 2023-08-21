package com.woowa.woowakit.domain.cart.domain;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.product.domain.product.ProductImage;
import com.woowa.woowakit.domain.product.domain.product.ProductName;
import com.woowa.woowakit.domain.product.domain.product.ProductPrice;

import lombok.Getter;

@Getter
public class CartItemSpecification {

	private final long quantity;
	private final long productPrice;
	private final String productName;
	private final String productImage;
	private final Long productId;

	public CartItemSpecification(
		final Quantity quantity,
		final ProductPrice productPrice,
		final ProductName productName,
		final ProductImage productImage,
		final Long productId
	) {
		this.quantity = quantity.getValue();
		this.productPrice = productPrice.getPrice().getValue();
		this.productName = productName.getName();
		this.productImage = productImage.getPath();
		this.productId = productId;
	}
}
