package com.woowa.woowakit.domain.auth.domain;

public interface PasswordEncoder {

    boolean matches(String planePassword, String encodedPassword);

    String encode(String password);
}
