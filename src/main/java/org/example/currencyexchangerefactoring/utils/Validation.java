package org.example.currencyexchangerefactoring.utils;

import org.example.currencyexchangerefactoring.exception.BadRequestException;
import org.example.currencyexchangerefactoring.exception.CurrencyFormatException;
import org.example.currencyexchangerefactoring.exception.ParameterLengthException;
import org.example.currencyexchangerefactoring.exception.SameCodesInRateException;
import org.example.currencyexchangerefactoring.filter.CurrencyContextListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

public class Validation {
    private static final int SIGN_LENGTH = 1;
    private static final int CURRENCY_NANE_MAX_LENGTH = 15;
    private static final int RECOMMENDED_SCALE = 6;


    public static void validate(String name, String code, String sign) throws IOException {
        if (name == null || code == null || sign == null || name.isBlank() || code.isBlank() || sign.isBlank()) {
            throw new BadRequestException();
        }

        if (!isValidCurrencyCode(code)) {
            throw new CurrencyFormatException();
        }

        if (!isCorrectLength(name, sign)) {
            throw new ParameterLengthException();
        }
    }

    public static void validate(String baseCode, String targetCode) throws IOException {
        NullOrBlank(baseCode, targetCode);
    }


    public static void validate(String baseCode, String targetCode, BigDecimal rate) throws IOException {
        NullOrBlank(baseCode, targetCode);

        if (!isValidRateOrAmount(rate)) {
            throw new ParameterLengthException();
        }
    }

    public static void validate(String code) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException();
        }
        if (!isValidCurrencyCode(code)) {
            throw new CurrencyFormatException();
        }
    }

    private static void NullOrBlank(String baseCode, String targetCode) {
        if (baseCode == null || targetCode == null  || baseCode.isBlank() || targetCode.isBlank()) {
            throw new BadRequestException();
        }

        if (!isValidCurrencyCode(targetCode)) {
            throw new CurrencyFormatException();
        }

        if (baseCode.equals(targetCode)) {
            throw new SameCodesInRateException();
        }
    }

    private static boolean isValidCurrencyCode(String code) {
        Set<String> currencyCodes = CurrencyContextListener.getCurrencyCodes();
        return currencyCodes.contains(code);
    }

    private static boolean isCorrectLength(String name, String sign) {
        return name.length() <= CURRENCY_NANE_MAX_LENGTH && sign.length() <= SIGN_LENGTH;
    }

    private static boolean isValidRateOrAmount(BigDecimal check) {
        if (check.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return check.scale() <= RECOMMENDED_SCALE;
    }
}
