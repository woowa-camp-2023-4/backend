package integration.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowa.woowakit.shop.auth.dto.request.LoginRequest;
import com.woowa.woowakit.shop.auth.dto.request.SignUpRequest;

import integration.IntegrationTest;
import integration.helper.CommonRestAssuredUtils;
import integration.helper.MemberHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class MemberIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("이메일, 비밀번호 닉네임이 입력되면 회원가입을 성공한다")
	void signUpSuccess() {
		// given
		SignUpRequest signUpRequest = SignUpRequest.of("email@woowa.com", "password", "name");

		// when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/auth/signup", signUpRequest);

		// then
		assertThat(response.jsonPath().getLong("id")).isNotZero();
		assertThat(response.statusCode()).isEqualTo(201);
	}

	@Test
	@DisplayName("이메일, 비밀번호를 입력하면 로그인이 성공하고 토큰을 반환한다")
	void loginSuccess() {
		// given
		MemberHelper.signup(MemberHelper.createSignUpRequest());
		LoginRequest loginRequest = LoginRequest.of("email@woowa.com", "password");

		//when
		ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/auth/login", loginRequest);

		//then
		assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
		assertThat(response.jsonPath().getString("name")).isNotBlank();
		assertThat(response.jsonPath().getString("email")).isNotBlank();
		assertThat(response.jsonPath().getString("role")).isNotBlank();
		assertThat(response.statusCode()).isEqualTo(200);
	}
}
