package com.woowa.woowakit.domains.cart.domain;

import com.woowa.woowakit.domains.model.Quantity;
import com.woowa.woowakit.domains.product.domain.product.ProductImage;
import com.woowa.woowakit.domains.product.domain.product.ProductName;
import com.woowa.woowakit.domains.product.domain.product.ProductPrice;

import lombok.Getter;

@Getter
public class CartItemSpecification {

	private final long quantity;
	private final long productPrice;
	private final String productName;
	private final String productImage;
	private final Long productId;
	private final Long cartItemId;

	public CartItemSpecification(
		final Quantity quantity,
		final ProductPrice productPrice,
		final ProductName productName,
		final ProductImage productImage,
		final Long productId,
		final Long cartItemId
	) {
		this.quantity = quantity.getValue();
		this.productPrice = productPrice.getPrice().getValue();
		this.productName = productName.getName();
		this.productImage = productImage.getPath();
		this.productId = productId;
		this.cartItemId = cartItemId;
	}
}
