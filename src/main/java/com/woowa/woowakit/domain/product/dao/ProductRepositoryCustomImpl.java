package com.woowa.woowakit.domain.product.dao;

import static com.woowa.woowakit.domain.product.domain.product.QProduct.*;
import static com.woowa.woowakit.domain.product.domain.product.QProductSales.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowa.woowakit.domain.product.domain.product.ProductSearchCondition;
import com.woowa.woowakit.domain.product.domain.product.ProductSpecification;
import com.woowa.woowakit.domain.product.domain.product.ProductStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ProductSpecification> searchProducts(final ProductSearchCondition condition) {
		return jpaQueryFactory.select(
				Projections.constructor(ProductSpecification.class,
					product,
					productSales.sale.value)
			)
			.from(product)
			.leftJoin(productSales).on(product.id.eq(productSales.productId)).fetchJoin()
			.where(
				containsName(condition.getProductKeyword()),
				saleNow(condition.getSaleDate()),
				sale(condition.getLastProductSale(), condition.getLastProductId()),
				product.status.eq(ProductStatus.IN_STOCK))
			.orderBy(productSales.sale.value.desc().nullsLast(), product.id.asc())
			.limit(condition.getPageSize())
			.fetch();
	}

	private BooleanExpression containsName(final String productKeyword) {
		if (StringUtils.hasText(productKeyword)) {
			return product.name.name.containsIgnoreCase(productKeyword);
		}

		return null;
	}

	private BooleanExpression saleNow(final LocalDate localDate) {
		return productSales.saleDate.eq(localDate).or(productSales.saleDate.isNull());
	}

	private BooleanExpression sale(final Long sale, final Long productId) {
		if (productId == null) {
			return null;
		}

		if (sale == null) {
			return product.id.gt(productId);
		}

		return productSales.sale.value.lt(sale).or(productSales.sale.value.eq(sale).and(product.id.gt(productId)));
	}
}
