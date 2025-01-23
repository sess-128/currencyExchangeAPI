package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangerefactoring.exception.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorHandlerRegistry {
    private static final Map<Class<? extends Exception>, ErrorHandler> handlers = new HashMap<>();

    static {
        handlers.put(CurrencyAlreadyExistException.class, new AlreadyExistsErrorHandler());
        handlers.put(ExchangeRateAlreadyExistException.class, new AlreadyExistsErrorHandler());
        handlers.put(CurrencyNotFoundException.class, new NotFoundErrorHandler());
        handlers.put(ExchangeRateNotFoundException.class, new NotFoundErrorHandler());
        handlers.put(NumberFormatException.class, new NotNumberErrorHandler());
        handlers.put(BadRequestException.class, new BadRequestErrorHandler());
        handlers.put(CurrencyFormatException.class, new CurrencyFormatErrorHandler());
        handlers.put(ParameterLengthException.class, new ParameterLengthErrorHandler());
        handlers.put(SameCodesInRateException.class, new SameCodesInRateErrorHandler());
    }

    public static ErrorHandler getHandler (Class<? extends Exception> exceptionClass) {
        return handlers.getOrDefault(exceptionClass, new DefaultErrorHandler());
    }

    public static class DefaultErrorHandler implements ErrorHandler {

        @Override
        public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        }
    }
}
