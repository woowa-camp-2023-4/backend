package com.woowa.woowakit.domain.member.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    @Email
    private String email;
    @NotBlank
    private String rawpassword;
    @NotBlank
    private String name;

    public static SignUpRequest of(String email, String password, String name) {
        return new SignUpRequest(email, password, name);
    }
}
