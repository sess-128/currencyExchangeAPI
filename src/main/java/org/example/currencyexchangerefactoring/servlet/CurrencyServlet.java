package org.example.currencyexchangerefactoring.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangerefactoring.dto.CurrencyDto;
import org.example.currencyexchangerefactoring.errorhandle.handlers.ErrorHandlerRegistry;
import org.example.currencyexchangerefactoring.exception.CurrencyNotFoundException;
import org.example.currencyexchangerefactoring.filter.CurrencyContextListener;
import org.example.currencyexchangerefactoring.service.CurrencyService;
import org.example.currencyexchangerefactoring.utils.Validation;

import java.io.IOException;

@WebServlet(name = "CurrencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyService currencyService = CurrencyService.getInstance();
    private final ObjectMapper objectMapper = CurrencyContextListener.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var code = req.getPathInfo().replaceAll("/", "");

        Validation.validate(code);

        try {
            CurrencyDto currencyDto = currencyService.findByCode(code)
                    .orElseThrow(CurrencyNotFoundException::new);

            resp.getWriter().write(objectMapper.writeValueAsString(currencyDto));

        } catch (Exception e) {
            ErrorHandlerRegistry.getHandler(e.getClass()).handle(e, req, resp);
        }
    }
}
