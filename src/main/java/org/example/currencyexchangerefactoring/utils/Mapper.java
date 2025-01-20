package org.example.currencyexchangerefactoring.utils;

import org.example.currencyexchangerefactoring.model.Currency;
import org.example.currencyexchangerefactoring.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper {
    public static Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }
    public static ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        Currency base = new Currency(
                resultSet.getLong("base_currency_id"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_currency_full_name"),
                resultSet.getString("base_currency_sign")
        );
        Currency target = new Currency(
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
}
