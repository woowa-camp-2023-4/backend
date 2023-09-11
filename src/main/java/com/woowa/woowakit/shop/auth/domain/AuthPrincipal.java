package com.woowa.woowakit.shop.auth.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AuthPrincipal {

	private Long id;
	private String email;
	private Role role;

	public static AuthPrincipal from(final Member member) {
		return new AuthPrincipal(member.getId(), member.getEmail().getValue(), member.getRole());
	}

	public boolean isAdmin() {
		return role.isAdmin();
	}
}
