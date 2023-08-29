package com.woowa.woowakit.infra.payment.toss;

import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.order.domain.PaymentClient;
import com.woowa.woowakit.infra.payment.toss.dto.TossPaymentErrorResponse;
import com.woowa.woowakit.infra.payment.toss.dto.TossPaymentRequest;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
public class TossPaymentClient implements PaymentClient {

	private final Duration timeout;
	private final int maxRetry;
	private final WebClient webClient;
	private final String paymentSecretKey;
	private final String url;

	public TossPaymentClient(
		final WebClient webClient,
		@Value("${payment.toss.secret-key}") final String paymentSecretKey,
		@Value("${payment.toss.confirm-url}") final String url,
		@Value("${payment.toss.timeout}") final int timeout,
		@Value("${payment.toss.max-retry}") final int maxRetry
	) {
		this.webClient = webClient;
		this.paymentSecretKey = paymentSecretKey;
		this.url = url;
		this.timeout = Duration.ofMillis(timeout);
		this.maxRetry = maxRetry;
	}

	public Mono<Void> validatePayment(final String paymentKey, final String orderToken,
		final Money totalPrice) {
		return webClient.post()
			.uri(url)
			.header(HttpHeaders.AUTHORIZATION, keyToBasic(paymentSecretKey))
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(TossPaymentRequest.of(paymentKey, orderToken, totalPrice))
			.retrieve()
			.onStatus(HttpStatus::is5xxServerError, this::handle5xxError)
			.onStatus(HttpStatus::is4xxClientError, this::handle4xxError)
			.toBodilessEntity()
			.timeout(timeout)
			.retryWhen(Retry.fixedDelay(maxRetry, timeout).filter(this::isRetryable))
			.onErrorMap(IllegalStateException.class, Throwable::getCause)
			.onErrorMap(TimeoutException.class,
				e -> new IllegalStateException("요청 시간이 초과되었습니다.", e))
			.then();
	}

	private boolean isRetryable(Throwable ex) {
		return (ex instanceof IllegalStateException) || (ex instanceof TimeoutException);
	}

	private Mono<IllegalStateException> handle5xxError(ClientResponse clientResponse) {
		return clientResponse.bodyToMono(TossPaymentErrorResponse.class)
			.map(TossPaymentErrorResponse::getMessage)
			.map(IllegalStateException::new);
	}

	private Mono<RuntimeException> handle4xxError(ClientResponse clientResponse) {
		return clientResponse.bodyToMono(TossPaymentErrorResponse.class)
			.map(response -> {
				if (Objects.equals(response.getCode(), "PROVIDER_ERROR")) {
					return new IllegalStateException(response.getMessage());
				}
				return new IllegalArgumentException(response.getMessage());
			});
	}

	private String keyToBasic(final String key) {
		return "Basic " + Base64.getEncoder().encodeToString((key + ":").getBytes());
	}
}
