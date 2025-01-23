package org.example.currencyexchangerefactoring.errorhandle.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ErrorHandler {
    void handle (Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
