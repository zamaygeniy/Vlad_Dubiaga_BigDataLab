package com.epam.crimes.exception;

public class UrlConnectionException extends Exception{
    public UrlConnectionException() {
        super();
    }

    public UrlConnectionException(String message) {
        super(message);
    }

    public UrlConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlConnectionException(Throwable cause) {
        super(cause);
    }
}
