package com.woowa.woowakit.domains.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignupResponse {

    private Long id;

    public static SignupResponse from(final Long id) {
        return new SignupResponse(id);
    }
}
