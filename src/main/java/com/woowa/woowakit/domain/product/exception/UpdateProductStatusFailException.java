package com.woowa.woowakit.domain.product.exception;

import org.springframework.http.HttpStatus;

public class UpdateProductStatusFailException extends ProductException {

    public UpdateProductStatusFailException() {
        super("재고가 0인 상태는 판매 중 상태로 변경할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
