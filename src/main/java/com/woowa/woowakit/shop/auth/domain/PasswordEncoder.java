package com.woowa.woowakit.shop.auth.domain;

public interface PasswordEncoder {

    boolean matches(final String planePassword, final String encodedPassword);

    String encode(final String password);
}
