package com.demo.exceptions;

public class ExchangeServiceException extends Exception {

    private static final long serialVersionUID = -561138078418617462L;

    private String message;

    public ExchangeServiceException() {
        super();
    }

    public ExchangeServiceException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
