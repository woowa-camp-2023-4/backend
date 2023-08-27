package com.woowa.woowakit.domain.order.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PreOrderCreateCartItemRequest {

	@NotNull(message = "장바구니는 필수 입니다.")
	private Long cartItemId;

	public static List<Long> toCartItemIds(final List<PreOrderCreateCartItemRequest> requests) {
		return requests.stream()
			.mapToLong(PreOrderCreateCartItemRequest::getCartItemId)
			.boxed()
			.collect(Collectors.toUnmodifiableList());
	}

	public static PreOrderCreateCartItemRequest from(Long cartItemId) {
		return new PreOrderCreateCartItemRequest(cartItemId);
	}
}
