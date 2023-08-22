package com.woowa.woowakit.global.error;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(
        IllegalArgumentException exception) {
        log.warn("잘못된 요청입니다.", exception);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "잘못된 요청입니다."));
    }

    @ExceptionHandler(WooWaException.class)
    public ResponseEntity<ErrorResponse> wooWaExceptionHandler(WooWaException exception) {
        log.info(exception.getMessage(), exception);
        return ResponseEntity
            .status(exception.getHttpStatus())
            .body(new ErrorResponse(exception.getHttpStatus().value(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
        MethodArgumentNotValidException exception) {
        String errorMessage = "입력값이 잘못되었습니다.\n";

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage + String.join(", ", getFieldErrorMessages(exception))
            ));
    }

    private List<String> getFieldErrorMessages(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream().map(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            return fieldName + ": " + message;
        }).collect(Collectors.toUnmodifiableList());
    }
}
