package com.woowa.woowakit.domain.product.domain.product;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;
import com.woowa.woowakit.domain.product.domain.product.converter.ProductImageConverter;
import com.woowa.woowakit.domain.product.domain.product.converter.ProductPriceConverter;
import com.woowa.woowakit.domain.product.exception.UpdateProductStatusFailException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

	private static final Quantity INITIAL_QUANTITY = Quantity.from(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private ProductName name;

	@Convert(converter = ProductPriceConverter.class)
	private ProductPrice price;

	@Convert(converter = ProductImageConverter.class)
	@Column(name = "image_url")
	private ProductImage imageUrl;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	@Convert(converter = QuantityConverter.class)
	private Quantity quantity;

	@Builder
	private Product(
		final Long id,
		final ProductName name,
		final ProductPrice price,
		final ProductImage imageUrl,
		final ProductStatus status,
		final Quantity quantity
	) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.status = status;
		this.quantity = quantity;
	}

	public static Product of(
		final String name,
		final Long price,
		final String imageUrl
	) {
		return Product.builder()
			.name(ProductName.from(name))
			.price(ProductPrice.from(price))
			.imageUrl(ProductImage.from(imageUrl))
			.status(ProductStatus.PRE_REGISTRATION)
			.quantity(INITIAL_QUANTITY)
			.build();
	}

	public void updateProductStatus(final ProductStatus productStatus) {
		if (Objects.equals(quantity, INITIAL_QUANTITY)) {
			throw new UpdateProductStatusFailException();
		}

		this.status = productStatus;
	}

	public boolean isOnSale() {
		return status == ProductStatus.IN_STOCK;
	}

	public void addQuantity(final Quantity quantity) {
		this.quantity = this.quantity.add(quantity);
	}

	public void subtractQuantity(final Quantity quantity) {
		this.quantity = this.quantity.subtract(quantity);

		if (this.quantity.isEmpty()) {
			this.status = ProductStatus.SOLD_OUT;
		}
	}

	public boolean isEnoughQuantity(final Quantity requiredQuantity) {
		return requiredQuantity.smallerThanOrEqualTo(this.quantity);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Product)) return false;
		final Product product = (Product) o;
		return Objects.equals(id, product.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
