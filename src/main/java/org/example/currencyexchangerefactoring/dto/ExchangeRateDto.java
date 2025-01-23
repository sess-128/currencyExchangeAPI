package org.example.currencyexchangerefactoring.dto;

import org.example.currencyexchangerefactoring.model.CurrencyE;

import java.math.BigDecimal;

public record ExchangeRateDto(Long id, CurrencyE baseCurrencyE, CurrencyE targetCurrencyE, BigDecimal rate) {}
