package com.woowa.woowakit.domain.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.woowa.woowakit.domain.product.domain.ProductImage;

@Converter
public class ProductImageConverter implements AttributeConverter<ProductImage, String> {

	// TODO: Environment 파일로 이동 후 외부에서 주입
	private static final String IMAGE_BASE_URL = "http://localhost:8080/";

	@Override
	public String convertToDatabaseColumn(final ProductImage attribute) {
		return attribute.getPath().replace(IMAGE_BASE_URL, "");
	}

	@Override
	public ProductImage convertToEntityAttribute(final String dbData) {
		return ProductImage.from(IMAGE_BASE_URL + dbData);
	}
}
