package com.woowa.woowakit.domain.member.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    private String email;
    private String password;
    private String name;

    public static SignUpRequest of(String email, String password, String name) {
        return new SignUpRequest(email, password, name);
    }
}
