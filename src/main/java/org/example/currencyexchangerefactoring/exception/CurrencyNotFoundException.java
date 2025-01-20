package org.example.currencyexchangerefactoring.exception;

public class CurrencyNotFoundException extends RuntimeException{
    public CurrencyNotFoundException(Throwable e){super(e);}
    public CurrencyNotFoundException(){}
}
