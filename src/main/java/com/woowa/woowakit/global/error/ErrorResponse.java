package com.woowa.woowakit.global.error;

public class ErrorResponse {

    private int statusCode;
    private String message;

    private ErrorResponse() {
    }

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
