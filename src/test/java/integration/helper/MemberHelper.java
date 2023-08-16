package integration.helper;

import com.woowa.woowakit.domain.auth.dto.request.SignUpRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemberHelper {

    public static <T> ExtractableResponse<Response> signup(SignUpRequest request) {
        return CommonRestAssuredUtils.post("/auth/signup", request);
    }

    public static SignUpRequest createSignUpRequest() {
        return SignUpRequest.of("email@woowa.com", "password", "name");
    }

}
