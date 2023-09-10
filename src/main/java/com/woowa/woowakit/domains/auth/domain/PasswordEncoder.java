package com.woowa.woowakit.domains.auth.domain;

public interface PasswordEncoder {

    boolean matches(final String planePassword, final String encodedPassword);

    String encode(final String password);
}
