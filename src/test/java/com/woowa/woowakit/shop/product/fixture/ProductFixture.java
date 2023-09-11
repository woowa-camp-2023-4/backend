package com.woowa.woowakit.shop.product.fixture;

import com.woowa.woowakit.shop.model.Quantity;
import com.woowa.woowakit.shop.product.domain.product.Product;
import com.woowa.woowakit.shop.product.domain.product.ProductImage;
import com.woowa.woowakit.shop.product.domain.product.ProductName;
import com.woowa.woowakit.shop.product.domain.product.ProductPrice;
import com.woowa.woowakit.shop.product.domain.product.ProductStatus;

public class ProductFixture {

	public static Product.ProductBuilder anProduct() {
		return Product.builder()
			.name(ProductName.from("우아한 밀키트"))
			.price(ProductPrice.from(10000L))
			.imageUrl(ProductImage.from("woowakit.img"))
			.quantity(Quantity.from(100L))
			.status(ProductStatus.IN_STOCK);
	}
}
