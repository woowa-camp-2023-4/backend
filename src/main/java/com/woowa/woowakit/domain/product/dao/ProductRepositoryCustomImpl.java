package com.woowa.woowakit.domain.product.dao;

import static com.woowa.woowakit.domain.product.domain.product.QProduct.product;

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
			.where(
				containsName(condition.getProductKeyword()),
				cursorId(condition.getLastProductId())
			)
			.limit(condition.getPageSize())
			.fetch();
	}

	private BooleanExpression containsName(final String productKeyword) {
		if (StringUtils.hasText(productKeyword)) {
			return product.name.name.containsIgnoreCase(productKeyword);
		}

		return null;
	}

	private BooleanExpression cursorId(final Long lastProductId) {
		if (lastProductId == null) {
			return null;
		}

		return product.id.gt(lastProductId);
	}
}
