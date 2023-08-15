package com.woowa.woowakit.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupResponse {

    private Long id;

    public static SignupResponse from(Long id) {
        return new SignupResponse(id);
    }
}
