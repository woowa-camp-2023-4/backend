package com.woowa.woowakit.domain.member.dto.request;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

    public static LoginRequest of(String email, String password) {
        return new LoginRequest(email, password);
    }
}
