package com.easychat.sse.exception;

public class CustomRuntimeException extends RuntimeException {

    public CustomRuntimeException(String message) {
        super(message);
    }

    public CustomRuntimeException(Throwable e) {
        super(e);
    }

    public CustomRuntimeException(String message, Exception e) {
        super(message, e);
    }
}
