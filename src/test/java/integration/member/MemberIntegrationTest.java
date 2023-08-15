package integration.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.woowakit.domain.member.dto.SignUpRequest;
import integration.IntegrationTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class MemberIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("이메일, 비밀번호 닉네임이 입력되면 회원가입을 성공한다")
    void signUpSuccess() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.of("email", "password", "name");

        // when
        ExtractableResponse<Response> response = post("/members/signup", signUpRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    public static <T> ExtractableResponse<Response> post(String url, T body) {
        return RestAssured.given().log().all()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(url)
            .then().log().all()
            .extract();
    }

}
