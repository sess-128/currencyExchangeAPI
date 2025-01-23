package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


import static org.example.currencyexchangerefactoring.errorhandle.ErrorCodesGetter.getMessage;

public class CurrencyFormatErrorHandler implements ErrorHandler {
    private static final int NOT_ISO_FORMAT = 4217;
    @Override
    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(getMessage(NOT_ISO_FORMAT));
    }
}
