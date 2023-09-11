package com.woowa.woowakit.shop.auth.dto.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {

    @Email
    @NotNull
    private String email;

    @NotBlank
    private String password;

    public static LoginRequest of(final String email, final String password) {
        return new LoginRequest(email, password);
    }
}
