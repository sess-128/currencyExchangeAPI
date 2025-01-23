package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.example.currencyexchangerefactoring.errorhandle.ErrorCodesGetter.getMessage;

public class NumberIncorrectErrorHandler implements ErrorHandler {
    private static final int NUMBER_INCORRECT_INPUT_ERROR = 888;
    @Override
    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(getMessage(NUMBER_INCORRECT_INPUT_ERROR));
    }
}
