package com.woowa.woowakit.domain.order.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderCreateRequest {

    @NotNull(message = "상품 아이디는 필수값입니다.")
    private Long productId;
    
    @NotNull(message = "수량은 필수값입니다.")
    private Long quantity;
}
