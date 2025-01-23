package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.example.currencyexchangerefactoring.errorhandle.ErrorCodesGetter.getMessage;

public class BadRequestErrorHandler implements ErrorHandler {
    @Override
    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(getMessage(resp));
    }
}
