package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.example.currencyexchangerefactoring.errorhandle.ErrorCodesGetter.getMessage;

public class NotNumberErrorHandler implements ErrorHandler {
    private static final int NUMBER_FORMAT_ERROR = 999;
    @Override
    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(getMessage(NUMBER_FORMAT_ERROR));
    }
}
