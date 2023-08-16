package com.woowa.woowakit.domain.auth.api;

import com.woowa.woowakit.domain.auth.application.AuthService;
import com.woowa.woowakit.domain.auth.dto.request.LoginRequest;
import com.woowa.woowakit.domain.auth.dto.response.LoginResponse;
import com.woowa.woowakit.domain.auth.dto.response.SignupResponse;
import com.woowa.woowakit.domain.auth.dto.request.SignUpRequest;
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

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        Long memberId = authService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(SignupResponse.from(memberId));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.loginMember(request);
        return ResponseEntity.ok(loginResponse);
    }
}
