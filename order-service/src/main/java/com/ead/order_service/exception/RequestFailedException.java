
package com.ead.order_service.exception;

public class RequestFailedException extends RuntimeException {
    public RequestFailedException(String message) {
        super(message);
    }

    public RequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}