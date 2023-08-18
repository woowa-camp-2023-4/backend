package com.woowa.woowakit.infra.payment.toss;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.woowakit.infra.payment.toss.dto.TossPaymentErrorResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest
class TossPaymentClientTest {

	@Autowired
	private TossPaymentClient client;

	@Autowired
	private ObjectMapper objectMapper;

	private MockWebServer webServer;

	@BeforeEach
	void setUp() throws IOException {
		webServer = new MockWebServer();
		webServer.start(8888);
	}

	@AfterEach
	void tearDown() throws IOException {
		webServer.shutdown();
	}

	@Test
	@DisplayName("Timeout에 의해 총 4번 재시도 후 실패하면 예외를 반환한다.")
	void validatePaymentErrorTimeout() {
		assertThatCode(() -> client.validatePayment("asdf", "asdf", 10L))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("요청 시간이 초과되었습니다.");
	}

	@Test
	@DisplayName("총 4번 재시도 수행해도 5xx 에러일 경우 예외를 반환한다.")
	void validatePaymentError5xx() throws JsonProcessingException {
		// given
		MockResponse response500 = response500();
		webServer.enqueue(response500);
		webServer.enqueue(response500);
		webServer.enqueue(response500);
		webServer.enqueue(response500);

		// when, then
		assertThatCode(() -> client.validatePayment("asdf", "asdf", 10L))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다.");
	}

	@Test
	@DisplayName("3번 실패 이후 마지막 4번쨰에 성공하면 성공.")
	void validatePaymentSuccessFinal5xx() throws JsonProcessingException {
		// given
		MockResponse failResponse = response500();
		MockResponse successResponse = response200();

		webServer.enqueue(failResponse);
		webServer.enqueue(failResponse);
		webServer.enqueue(failResponse);
		webServer.enqueue(successResponse);

		// when, then
		assertThatCode(() -> client.validatePayment("asdf", "asdf", 10L))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("PROVIDER_ERROR가 아닌 4xx에러의 경우 재시도 없이 실패한다.")
	void validatePayment400NotProviderError() throws JsonProcessingException {
		MockResponse failResponse = response400();
		MockResponse successResponse = response200();

		webServer.enqueue(failResponse);
		webServer.enqueue(successResponse);

		assertThatCode(() -> client.validatePayment("asdf", "asdf", 10L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 요청입니다.");
	}

	@Test
	@DisplayName("PROVIDER_ERROR인 경우 재시도를 한다.")
	void validate400ProviderError() throws JsonProcessingException {
		// given
		MockResponse failResponse = response400ProviderError();
		webServer.enqueue(failResponse);
		webServer.enqueue(failResponse);
		webServer.enqueue(failResponse);
		webServer.enqueue(failResponse);

		// when, then
		assertThatCode(() -> client.validatePayment("asdf", "asdf", 10L))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
	}

	private MockResponse response200() {
		return new MockResponse()
			.setResponseCode(200)
			.setHeader("Content-Type", "application/json");
	}

	private MockResponse response400ProviderError() throws JsonProcessingException {
		String responseString = objectMapper.writeValueAsString(
			TossPaymentErrorResponse.of("PROVIDER_ERROR", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));

		return new MockResponse()
			.setResponseCode(400)
			.setHeader("Content-Type", "application/json")
			.setBody(responseString);
	}

	private MockResponse response400() throws JsonProcessingException {
		String responseString = objectMapper.writeValueAsString(
			TossPaymentErrorResponse.of("INVALID_REQUEST", "잘못된 요청입니다."));

		return new MockResponse()
			.setResponseCode(400)
			.setHeader("Content-Type", "application/json")
			.setBody(responseString);
	}

	private MockResponse response500() throws JsonProcessingException {
		String responseString = objectMapper.writeValueAsString(
			TossPaymentErrorResponse.of("NOT_FOUND_PAYMENT_SESSION", "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다."));

		return new MockResponse()
			.setResponseCode(500)
			.setHeader("Content-Type", "application/json")
			.setBody(responseString);
	}
}
