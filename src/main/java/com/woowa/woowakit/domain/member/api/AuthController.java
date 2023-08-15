package com.woowa.woowakit.domain.member.api;

import com.woowa.woowakit.domain.member.application.AuthSerivce;
import com.woowa.woowakit.domain.member.dto.SignUpRequest;
import com.woowa.woowakit.domain.member.dto.response.SignupResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthSerivce authSerivce;

    //signup
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        Long memberId = authSerivce.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(SignupResponse.from(memberId));
    }

}
