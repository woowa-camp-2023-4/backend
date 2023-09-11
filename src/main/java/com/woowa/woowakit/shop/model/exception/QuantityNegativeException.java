package com.woowa.woowakit.shop.model.exception;

import com.woowa.woowakit.global.error.WooWaException;
import org.springframework.http.HttpStatus;

public class QuantityNegativeException extends WooWaException {

        public QuantityNegativeException() {
            super("제품 수량은 0보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
}
