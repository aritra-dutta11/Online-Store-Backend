package com.backend.demoBackend.Exception;

import lombok.Getter;

@Getter
public class PlaceOrderException extends RuntimeException {
    private String errorMsg;
    private String errorCode;

    public PlaceOrderException(
            String errorMsg,
            String errorCode) {
        super(errorMsg);
        this.errorCode = errorCode;
    }
}
