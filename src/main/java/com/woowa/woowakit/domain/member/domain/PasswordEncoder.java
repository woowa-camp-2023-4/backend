package com.woowa.woowakit.domain.member.domain;

public interface PasswordEncoder {

    boolean matches(String planePassword, String encodedPassword);

    String encode(String password);
}
