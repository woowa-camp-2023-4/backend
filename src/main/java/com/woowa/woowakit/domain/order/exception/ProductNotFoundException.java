package com.woowa.woowakit.domain.order.exception;

import com.woowa.woowakit.global.error.WooWaException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends WooWaException {

    public ProductNotFoundException() {
        super("존재하지 않은 상품 정보입니다.", HttpStatus.BAD_REQUEST);
    }

}
