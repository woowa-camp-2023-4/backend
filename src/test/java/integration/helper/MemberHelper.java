package integration.helper;

import com.woowa.woowakit.domain.auth.dto.request.LoginRequest;
import com.woowa.woowakit.domain.auth.dto.request.SignUpRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemberHelper {

    public static ExtractableResponse<Response> signup(final SignUpRequest request) {
        return CommonRestAssuredUtils.post("/auth/signup", request);
    }

    public static SignUpRequest createSignUpRequest() {
        return SignUpRequest.of("email@woowa.com", "password", "name");
    }

    public static LoginRequest createLoginRequest() {
        return LoginRequest.of("email@woowa.com", "password");
    }

    public String login(final SignUpRequest request) {
        return CommonRestAssuredUtils.post("/auth/login", request)
                .jsonPath()
                .getString("accessToken");
    }
}
