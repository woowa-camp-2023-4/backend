package com.woowa.woowakit.domains.product.domain.product;

import com.woowa.woowakit.domains.model.BaseEntity;
import com.woowa.woowakit.domains.model.Quantity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "product_sales")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductSales extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id")
	private Long productId;

	@Embedded
	private SaleQuantity sale;

	@Column(name = "sale_date")
	private LocalDate saleDate;

	@Builder
	public ProductSales(
		final Long productId,
		final Quantity sale,
		final LocalDate saleDate
	) {
		this.productId = productId;
		this.sale = SaleQuantity.from(sale.getValue());
		this.saleDate = saleDate;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductSales)) return false;
		final ProductSales that = (ProductSales) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
