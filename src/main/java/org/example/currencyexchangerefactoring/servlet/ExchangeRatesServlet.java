package org.example.currencyexchangerefactoring.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangerefactoring.dto.ExchangeRateDto;
import org.example.currencyexchangerefactoring.errorhandle.handlers.ErrorHandlerRegistry;
import org.example.currencyexchangerefactoring.filter.CurrencyContextListener;
import org.example.currencyexchangerefactoring.service.ExchangeRateService;
import org.example.currencyexchangerefactoring.utils.Mapper;
import org.example.currencyexchangerefactoring.utils.Validation;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "ExchangeRatesServlet", urlPatterns = "/exchangeRates/*")
public class ExchangeRatesServlet  extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = CurrencyContextListener.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateDto> exchangeRateDto = exchangeRateService.findAll();

        String jsonResponse = new ObjectMapper().writeValueAsString(exchangeRateDto);

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate2 = req.getParameter("rate");
        BigDecimal rate = new BigDecimal(rate2);

        Validation.validate(baseCurrencyCode, targetCurrencyCode, rate);

        try {
            Mapper.buildExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            ExchangeRateDto save = exchangeRateService.save(baseCurrencyCode, targetCurrencyCode, rate);
            resp.getWriter().write(objectMapper.writeValueAsString(save));

        } catch (Exception e) {
            ErrorHandlerRegistry.getHandler(e.getClass()).handle(e, req, resp);
        }

    }
}
