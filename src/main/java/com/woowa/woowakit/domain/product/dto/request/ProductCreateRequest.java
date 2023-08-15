package com.woowa.woowakit.domain.product.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.woowa.woowakit.domain.product.domain.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class ProductCreateRequest {

	@NotBlank
	private String name;
	@NotNull
	@PositiveOrZero
	private Long price;
	private String imageUrl;

	public Product toEntity() {
		return Product.of(name, price, imageUrl);
	}
}
