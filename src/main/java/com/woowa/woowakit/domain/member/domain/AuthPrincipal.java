package com.woowa.woowakit.domain.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AuthPrincipal {

    private final Long id;
    private final String email;
    private final Role role;

    public static AuthPrincipal from(final Member member) {
        return new AuthPrincipal(member.getId(), member.getEmail().getValue(), member.getRole());
    }
}
