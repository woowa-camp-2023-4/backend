package com.woowa.woowakit.domain.product.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.woowa.woowakit.domain.product.domain.product.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductCreateRequest {

	@NotBlank(message = "이름은 공백일 수 없습니다.")
	private String name;

	@NotNull(message = "가격은 필수 항목입니다.")
	@PositiveOrZero(message = "가격이 음수일 수 없습니다.")
	private Long price;

	private String imageUrl;

	public static ProductCreateRequest of(final String name, final Long price, final String imageUrl) {
		return new ProductCreateRequest(name, price, imageUrl);
	}

	public Product toEntity() {
		return Product.of(name, price, imageUrl);
	}
}
