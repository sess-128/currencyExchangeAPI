package org.example.currencyexchangerefactoring.exception;

public class CurrencyAlreadyExistException extends RuntimeException{
    public CurrencyAlreadyExistException(Throwable e){super(e);}
}
