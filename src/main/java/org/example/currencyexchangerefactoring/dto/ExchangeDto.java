package org.example.currencyexchangerefactoring.dto;

import org.example.currencyexchangerefactoring.model.Currency;

import java.math.BigDecimal;

public record ExchangeDto(Currency baseCurrency,
                          Currency targetCurrency,
                          BigDecimal rate,
                          BigDecimal amount,
                          BigDecimal convertedAmount) {
}
