package com.woowa.woowakit.domains.auth.domain;

import lombok.Getter;

@Getter
public enum Role {
	ADMIN, USER;

	public boolean isAdmin() {
		return this == ADMIN;
	}
}
