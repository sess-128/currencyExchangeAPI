package org.example.currencyexchangerefactoring.utils;

import org.example.currencyexchangerefactoring.dto.ExchangeDto;
import org.example.currencyexchangerefactoring.dto.ExchangeRateDto;
import org.example.currencyexchangerefactoring.model.CurrencyE;
import org.example.currencyexchangerefactoring.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper {
    public static CurrencyE buildCurrency(ResultSet resultSet) throws SQLException {
        return new CurrencyE(resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }

    public static ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        CurrencyE base = new CurrencyE(
                resultSet.getLong("base_currency_id"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_currency_full_name"),
                resultSet.getString("base_currency_sign")
        );
        CurrencyE target = new CurrencyE(
                resultSet.getLong("target_currency_id"),
                resultSet.getString("target_currency_code"),
                resultSet.getString("target_currency_full_name"),
                resultSet.getString("target_currency_sign")
        );
        return new ExchangeRate(
                resultSet.getLong("exchange_rate_id"),
                base,
                target,
                resultSet.getBigDecimal("rate")
        );
    }

    public static ExchangeRate buildExchangeRate(CurrencyE baseCurrency, CurrencyE targetCurrency, BigDecimal rate) {
        return new ExchangeRate(
                baseCurrency,
                targetCurrency,
                rate
        );
    }

    public static ExchangeRateDto buildExchangeRateDto(ExchangeRate exchangeRate) {
        return new ExchangeRateDto(
                exchangeRate.getId(),
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate().setScale(2, RoundingMode.HALF_DOWN)
        );
    }

    public static ExchangeDto buildExchangeDto(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertedAmount) {
        return new ExchangeDto(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate(),
                amount,
                convertedAmount.setScale(2, RoundingMode.HALF_DOWN)

        );
    }
}
