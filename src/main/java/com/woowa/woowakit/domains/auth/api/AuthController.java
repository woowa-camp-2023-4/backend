package com.woowa.woowakit.domains.auth.api;

import com.woowa.woowakit.domains.auth.application.AuthService;
import com.woowa.woowakit.domains.auth.dto.request.LoginRequest;
import com.woowa.woowakit.domains.auth.dto.request.SignUpRequest;
import com.woowa.woowakit.domains.auth.dto.response.LoginResponse;
import com.woowa.woowakit.domains.auth.dto.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signUp(@Valid @RequestBody final SignUpRequest request) {
        final Long memberId = authService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(SignupResponse.from(memberId));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request) {
        final LoginResponse loginResponse = authService.loginMember(request);

        return ResponseEntity.ok(loginResponse);
    }
}
