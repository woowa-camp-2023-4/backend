package com.woowa.woowakit.domain.product.dto.request;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.woowa.woowakit.domain.product.domain.product.ProductSearchCondition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductSearchRequest {

	private static final int DEFAULT_PAGE_SIZE = 10;

	private String productKeyword;

	private Long lastProductId;

	private Long lastProductSale;

	@Min(value = 1, message = "최소 1개 이상의 상품을 조회해야합니다.")
	private int pageSize = DEFAULT_PAGE_SIZE;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleDate = LocalDate.now().minusDays(1);

	public static ProductSearchRequest of(
		final String productKeyword,
		final Long lastProductId,
		final Long lastProductSale,
		final int pageSize,
		final LocalDate saleDate
	) {
		return new ProductSearchRequest(productKeyword, lastProductId, lastProductSale, pageSize, saleDate);
	}

	public ProductSearchCondition toProductSearchCondition() {
		return ProductSearchCondition.of(productKeyword, lastProductId, lastProductSale, pageSize, saleDate);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductSearchRequest that = (ProductSearchRequest)o;
		return pageSize == that.pageSize && Objects.equals(productKeyword, that.productKeyword)
			&& Objects.equals(lastProductId, that.lastProductId) && Objects.equals(lastProductSale,
			that.lastProductSale) && Objects.equals(saleDate, that.saleDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productKeyword, lastProductId, lastProductSale, pageSize, saleDate);
	}

	@Override
	public String toString() {
		return "ProductSearchRequest{" +
			"productKeyword='" + productKeyword + '\'' +
			", lastProductId=" + lastProductId +
			", lastProductSale=" + lastProductSale +
			", pageSize=" + pageSize +
			", saleDate=" + saleDate +
			'}';
	}
}


