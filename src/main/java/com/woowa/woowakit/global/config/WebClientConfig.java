package com.woowa.woowakit.global.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {

	private static final int TIMEOUT_MILLIS = 3000;

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
			.filter(logRequest())
			.filter(logResponse())
			.clientConnector(new ReactorClientHttpConnector(httpClient()))
			.build();
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_MILLIS)
			.responseTimeout(Duration.ofMillis(TIMEOUT_MILLIS))
			.doOnConnected(conn -> conn
				.addHandlerLast(new ReadTimeoutHandler(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS))
				.addHandlerLast(new WriteTimeoutHandler(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)));
	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			log.info("Request: {} {}\n", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
				.forEach((name, values) -> values.forEach(
					value -> log.info("Request Header: {}={}", name, value)));
			return Mono.just(clientRequest);
		});
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			log.info("Response Status: {}\n", clientResponse.statusCode());
			clientResponse.headers()
				.asHttpHeaders()
				.forEach((name, values) -> values.forEach(
					value -> log.info("Response Header {}={}", name, value)));
			return Mono.just(clientResponse);
		});
	}
}
