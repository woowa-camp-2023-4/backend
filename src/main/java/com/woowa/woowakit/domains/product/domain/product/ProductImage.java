package com.woowa.woowakit.domains.product.domain.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductImage {

	private final String path;

	public static ProductImage from(final String path) {
		return new ProductImage(path);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductImage)) return false;
		final ProductImage that = (ProductImage) o;
		return Objects.equals(path, that.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path);
	}
}
