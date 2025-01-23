package org.example.currencyexchangerefactoring.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangerefactoring.dto.ExchangeRateDto;
import org.example.currencyexchangerefactoring.errorhandle.handlers.ErrorHandlerRegistry;
import org.example.currencyexchangerefactoring.exception.BadRequestException;
import org.example.currencyexchangerefactoring.exception.ExchangeRateNotFoundException;
import org.example.currencyexchangerefactoring.filter.CurrencyContextListener;
import org.example.currencyexchangerefactoring.service.ExchangeRateService;
import org.example.currencyexchangerefactoring.utils.Validation;

import java.io.IOException;
import java.math.BigDecimal;

import static org.example.currencyexchangerefactoring.errorhandle.ErrorCodesGetter.getMessage;

@WebServlet(name = "ExchangeRateServlet", urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = CurrencyContextListener.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var codes = req.getPathInfo().replaceAll("/", "");

        if (codes.length() != 6) {
            throw new BadRequestException();
        }

        String baseCode = codes.substring(0, 3);
        String targetCode = codes.substring(3);

        Validation.validate(baseCode, targetCode);

        ExchangeRateDto optionalExchangeRateDto = exchangeRateService.findByPair(baseCode, targetCode)
                .orElseThrow(ExchangeRateNotFoundException::new);
        try {
            resp.getWriter().write(objectMapper.writeValueAsString(optionalExchangeRateDto));
        } catch (Exception e) {
            ErrorHandlerRegistry.getHandler(e.getClass()).handle(e, req, resp);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathInfo = req.getPathInfo();

        if (pathInfo.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(getMessage(resp));
            return;
        }

        String baseCurrencyCode = pathInfo.substring(1, 4);
        String targetCurrencyCode = pathInfo.substring(4);
        String stringRate = req.getReader().readLine().substring(5);
        BigDecimal rates = new BigDecimal(stringRate);

        Validation.validate(baseCurrencyCode, targetCurrencyCode, rates);

        try {

            ExchangeRateDto updated = exchangeRateService.update(baseCurrencyCode, targetCurrencyCode, rates);
            resp.getWriter().write(objectMapper.writeValueAsString(updated));

        } catch (Exception e) {
            ErrorHandlerRegistry.getHandler(e.getClass()).handle(e, req, resp);
        }

    }
}
