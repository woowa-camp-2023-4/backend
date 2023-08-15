package com.woowa.woowakit.domain.member.dto.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {

    private String email;
    private String password;

    public static LoginRequest of(String email, String password) {
        return new LoginRequest(email, password);
    }
}
