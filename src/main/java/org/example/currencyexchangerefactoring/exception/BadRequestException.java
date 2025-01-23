package org.example.currencyexchangerefactoring.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(Throwable throwable) {
        super(throwable);
    }

    public BadRequestException() {
    }
}
