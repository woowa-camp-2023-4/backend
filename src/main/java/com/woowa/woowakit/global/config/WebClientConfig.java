package com.woowa.woowakit.global.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	private static final int TIMEOUT_MILLIS = 1000;

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
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
}
