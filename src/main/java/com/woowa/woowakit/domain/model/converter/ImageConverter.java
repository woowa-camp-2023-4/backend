package com.woowa.woowakit.domain.model.converter;

import com.woowa.woowakit.domain.model.Image;
import javax.persistence.AttributeConverter;

public class ImageConverter implements AttributeConverter<Image, String> {

    @Override
    public String convertToDatabaseColumn(final Image image) {
        return image.getValue();
    }

    @Override
    public Image convertToEntityAttribute(final String image) {
        return Image.from(image);
    }

}
