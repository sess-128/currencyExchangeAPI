package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.example.currencyexchangerefactoring.errorhandle.ErrorCodesGetter.getMessage;

public class ParameterLengthErrorHandler implements ErrorHandler {
    private static final int MAX_LENGTH_ERROR = 777;
    @Override
    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(getMessage(MAX_LENGTH_ERROR));
    }
}
