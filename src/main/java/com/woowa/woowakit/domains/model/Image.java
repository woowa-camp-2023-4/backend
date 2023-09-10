package com.woowa.woowakit.domains.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class Image {

	private final String value;

	public static Image from(final String value) {
		return new Image(value);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Image)) return false;
		final Image image = (Image) o;
		return Objects.equals(value, image.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
