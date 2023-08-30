package com.woowa.woowakit.domain.auth.dto.response;

import com.woowa.woowakit.domain.auth.domain.Email;
import com.woowa.woowakit.domain.auth.domain.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponse {

	private String accessToken;
	private String name;
	private String role;
	private String email;

	public static LoginResponse of(
		final String accessToken,
		final String name,
		final Role role,
		final Email email
	) {
		return new LoginResponse(accessToken, name, role.name(), email.getValue());
	}
}
