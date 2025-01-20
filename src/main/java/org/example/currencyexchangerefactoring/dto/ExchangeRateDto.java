package org.example.currencyexchangerefactoring.dto;

import org.example.currencyexchangerefactoring.model.Currency;

import java.math.BigDecimal;

public record ExchangeRateDto(Long id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {}
