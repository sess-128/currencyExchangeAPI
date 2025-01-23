package org.example.currencyexchangerefactoring.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@WebListener
public class CurrencyContextListener implements ServletContextListener {
    private static Set<String> currencyCodes;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void contextInitialized (ServletContextEvent sce) {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        currencyCodes = new HashSet<>();

        for (Currency currency : currencies) {
            currencyCodes.add(currency.getCurrencyCode());
        }
    }

    public static Set<String> getCurrencyCodes () {
        return  currencyCodes;
    }

    public static ObjectMapper getObjectMapper () {
        return objectMapper;
    }
}
