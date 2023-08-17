package com.woowa.woowakit.domain.auth.dto.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

    public static LoginRequest of(final String email, final String password) {
        return new LoginRequest(email, password);
    }
}
