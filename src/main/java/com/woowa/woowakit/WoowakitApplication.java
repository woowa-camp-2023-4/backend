package com.woowa.woowakit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WoowakitApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoowakitApplication.class, args);
	}

}
