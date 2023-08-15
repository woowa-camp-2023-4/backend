package com.woowa.woowakit.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(RuntimeException exception) {
        log.error("예상하지 못한 에러입니다.", exception);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "예상하지 못한 에러입니다."));
    }

    @ExceptionHandler(WooWaException.class)
    public ResponseEntity<ErrorResponse> wooWaExceptionHandler(WooWaException exception) {
        log.info(exception.getMessage(), exception);
        return ResponseEntity
            .status(exception.getHttpStatus())
            .body(new ErrorResponse(exception.getHttpStatus().value(), exception.getMessage()));
    }
}
