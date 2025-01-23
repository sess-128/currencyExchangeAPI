package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangerefactoring.exception.CurrencyAlreadyExistException;

import java.io.IOException;

import static org.example.currencyexchangerefactoring.errorhandle.ErrorCodesGetter.getMessage;

public class AlreadyExistsErrorHandler implements ErrorHandler {
    @Override
    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (e instanceof CurrencyAlreadyExistException) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(getMessage(resp));
        }
    }
}
