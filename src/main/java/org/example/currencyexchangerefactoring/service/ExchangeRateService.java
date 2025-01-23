package org.example.currencyexchangerefactoring.service;

import org.example.currencyexchangerefactoring.dao.ExchangeRateDao;
import org.example.currencyexchangerefactoring.dto.ExchangeDto;
import org.example.currencyexchangerefactoring.dto.ExchangeRateDto;
import org.example.currencyexchangerefactoring.exception.ExchangeRateNotFoundException;
import org.example.currencyexchangerefactoring.model.ExchangeRate;
import org.example.currencyexchangerefactoring.utils.Mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static java.math.MathContext.DECIMAL64;
import static java.util.stream.Collectors.toList;

public class ExchangeRateService {
    private static final String CURRENCY_USD_CODE = "USD";
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();

    private ExchangeRateService() {
    }

    public List<ExchangeRateDto> findAll() {
        return exchangeRateDao.findAll().stream()
                .map(exchangeRate -> new ExchangeRateDto(
                        exchangeRate.getId(), exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate().setScale(2, RoundingMode.HALF_DOWN)
                ))
                .collect(toList());
    }

    public Optional<ExchangeRateDto> findByPair(String base, String target) {
        return exchangeRateDao.findByPair(base, target)
                .map(exchangeRate -> new ExchangeRateDto(
                        exchangeRate.getId(), exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate().setScale(2, RoundingMode.HALF_DOWN)
                ));
    }

    public ExchangeRateDto save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rates) {
        ExchangeRate savedExchangeRate = exchangeRateDao.save(rates, baseCurrencyCode, targetCurrencyCode);
        return Mapper.buildExchangeRateDto(savedExchangeRate);
    }

    public ExchangeRateDto update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rates) {
        ExchangeRate exchangeRate = exchangeRateDao.findByPair(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(ExchangeRateNotFoundException::new);

        exchangeRate.setRate(rates);

        exchangeRateDao.update(exchangeRate);
        return Mapper.buildExchangeRateDto(exchangeRate);
    }

    public ExchangeDto convert(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        ExchangeRate exchangeRate = getSimpleExchange(baseCurrencyCode, targetCurrencyCode);

        BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());

        return Mapper.buildExchangeDto(exchangeRate, amount, convertedAmount);
    }

    private ExchangeRate getSimpleExchange(String baseCurrencyCode, String targetCurrencyCode) {
        return exchangeRateDao.findByPair(baseCurrencyCode, targetCurrencyCode)
                .map(exchangeRate -> Mapper.buildExchangeRate(
                        exchangeRate.getTargetCurrency(),
                        exchangeRate.getBaseCurrency(),
                        exchangeRate.getRate()
                ))
                .or(() -> exchangeRateDao.findByPair(targetCurrencyCode, baseCurrencyCode)
                        .map(exchangeRate -> {
                            BigDecimal invertedRate = BigDecimal.ONE.divide(exchangeRate.getRate(), DECIMAL64);
                            return Mapper.buildExchangeRate(
                                    exchangeRate.getTargetCurrency(),
                                    exchangeRate.getBaseCurrency(),
                                    invertedRate
                            );
                        }))
                .or(() -> getCrossExchange(baseCurrencyCode, targetCurrencyCode))
                .or(() -> getCrossExchange(targetCurrencyCode, baseCurrencyCode))
                .orElseThrow(() -> new IllegalArgumentException("No exchange rate available for the provided currency codes"));
    }

    private Optional<ExchangeRate> getCrossExchange(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate fromUSDToBase = exchangeRateDao.findByPair(CURRENCY_USD_CODE, baseCurrencyCode).orElseThrow(ExchangeRateNotFoundException::new);
        ExchangeRate fromUSDToTarget = exchangeRateDao.findByPair(CURRENCY_USD_CODE, targetCurrencyCode).orElseThrow(ExchangeRateNotFoundException::new);

        BigDecimal usdToBaseRate = fromUSDToBase.getRate();
        BigDecimal usdToTargetRate = fromUSDToTarget.getRate();

        BigDecimal baseToTargetRate = usdToTargetRate.divide(usdToBaseRate, DECIMAL64);

        return Optional.of(Mapper.buildExchangeRate(
                fromUSDToBase.getTargetCurrency(),
                fromUSDToTarget.getTargetCurrency(),
                baseToTargetRate
        ));
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
