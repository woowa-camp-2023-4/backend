package com.woowa.woowakit.shop.auth.domain;

import lombok.Getter;

@Getter
public enum Role {
	ADMIN, USER;

	public boolean isAdmin() {
		return this == ADMIN;
	}
}
