package com.woowa.woowakit.domain.product.dao;

import static com.woowa.woowakit.domain.product.domain.product.QProduct.*;
import static com.woowa.woowakit.domain.product.domain.product.QProductSales.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductSearchCondition;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Product> searchProducts(final ProductSearchCondition condition) {
		return jpaQueryFactory.selectFrom(product)
			.leftJoin(productSales).on(product.id.eq(productSales.productId))
			.where(
				containsName(condition.getProductKeyword()),
				saleNow(condition.getSaleDate()),
				cursorId(condition.getLastProductId()))
			.limit(condition.getPageSize())
			.orderBy(productSales.sale.value.desc().nullsLast(), product.id.asc())
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

	private BooleanExpression cursorId(final Long lastProductId) {
		if (lastProductId == null) {
			return null;
		}

		return product.id.gt(lastProductId);
	}
}
