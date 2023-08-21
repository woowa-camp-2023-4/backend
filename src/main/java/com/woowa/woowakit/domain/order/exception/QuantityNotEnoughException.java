package com.woowa.woowakit.domain.order.exception;

import com.woowa.woowakit.global.error.WooWaException;
import org.springframework.http.HttpStatus;

public class QuantityNotEnoughException extends WooWaException {

    public QuantityNotEnoughException() {
        super("상품의 재고가 부족합니다", HttpStatus.CONFLICT);
    }

}
