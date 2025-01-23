package org.example.currencyexchangerefactoring.dto;

import org.example.currencyexchangerefactoring.model.CurrencyE;

import java.math.BigDecimal;

public record ExchangeDto(CurrencyE baseCurrencyE,
                          CurrencyE targetCurrencyE,
                          BigDecimal rate,
                          BigDecimal amount,
                          BigDecimal convertedAmount) {
}
