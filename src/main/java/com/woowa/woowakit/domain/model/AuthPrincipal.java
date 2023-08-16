package com.woowa.woowakit.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AuthPrincipal {

    private final Long id;

    public static AuthPrincipal from(Long id) {
        return new AuthPrincipal(id);
    }
}
