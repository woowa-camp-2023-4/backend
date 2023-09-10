package com.woowa.woowakit.domains.product.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.woowakit.domains.product.application.StockScheduler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor
public class StockSchedulerController {

	private final StockScheduler stockScheduler;

	@GetMapping
	public String trigger() {
		stockScheduler.trigger();
		return "OK";
	}
}
