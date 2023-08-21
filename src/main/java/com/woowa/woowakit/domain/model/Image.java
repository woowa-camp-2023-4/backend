package com.woowa.woowakit.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class Image {

    private final String value;

    public static Image from(final String value) {
        return new Image(value);
    }
}
