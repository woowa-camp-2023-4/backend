package com.woowa.woowakit.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignupResponse {

    private Long id;

    public static SignupResponse from(Long id) {
        return new SignupResponse(id);
    }
}
