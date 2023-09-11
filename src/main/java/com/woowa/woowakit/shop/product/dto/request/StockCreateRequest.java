package com.woowa.woowakit.shop.product.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StockCreateRequest {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
	private Long quantity;

	public static StockCreateRequest of(final LocalDate expiryDate, final Long quantity) {
		return new StockCreateRequest(expiryDate, quantity);
	}
}
