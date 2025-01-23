package org.example.currencyexchangerefactoring.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangerefactoring.dto.CurrencyDto;
import org.example.currencyexchangerefactoring.errorhandle.handlers.ErrorHandlerRegistry;
import org.example.currencyexchangerefactoring.filter.CurrencyContextListener;
import org.example.currencyexchangerefactoring.model.CurrencyE;
import org.example.currencyexchangerefactoring.service.CurrencyService;
import org.example.currencyexchangerefactoring.utils.Validation;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CurrenciesServlet", urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final ObjectMapper objectMapper = CurrencyContextListener.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<CurrencyDto> currenciesDto = currencyService.findAll();

        String jsonResponse = new ObjectMapper().writeValueAsString(currenciesDto);

        try (var printWriter = resp.getWriter()) {
            printWriter.write(jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        Validation.validate(name, code, sign);

        try {
            CurrencyDto currencyDto = currencyService.save(new CurrencyE(code, name, sign));
            resp.getWriter().write(objectMapper.writeValueAsString(currencyDto));

        } catch (Exception e) {
            ErrorHandlerRegistry.getHandler(e.getClass()).handle(e, req, resp);
        }
    }


}
