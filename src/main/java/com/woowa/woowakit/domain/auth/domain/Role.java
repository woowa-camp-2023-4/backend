package com.woowa.woowakit.domain.auth.domain;

import lombok.Getter;

@Getter
public enum Role {
	ADMIN, USER;

	public boolean isAdmin() {
		return this == ADMIN;
	}
}
