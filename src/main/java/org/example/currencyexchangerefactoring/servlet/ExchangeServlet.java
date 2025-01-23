package org.example.currencyexchangerefactoring.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangerefactoring.dto.ExchangeDto;
import org.example.currencyexchangerefactoring.errorhandle.handlers.ErrorHandlerRegistry;
import org.example.currencyexchangerefactoring.filter.CurrencyContextListener;
import org.example.currencyexchangerefactoring.service.ExchangeRateService;
import org.example.currencyexchangerefactoring.utils.Validation;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "ExchangeServlet", urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = CurrencyContextListener.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String sAmount = req.getParameter("amount");
        BigDecimal amount = new BigDecimal(sAmount);

        Validation.validate(baseCurrencyCode, targetCurrencyCode, amount);

        try {

            ExchangeDto converted = exchangeRateService.convert(baseCurrencyCode, targetCurrencyCode, amount);
            resp.getWriter().write(objectMapper.writeValueAsString(converted));
        } catch (Exception e) {
            ErrorHandlerRegistry.getHandler(e.getClass()).handle(e, req, resp);
        }
    }
}

