package org.example.currencyexchangerefactoring.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRate {
    private Long id;
    private CurrencyE baseCurrencyE;
    private CurrencyE targetCurrencyE;
    private BigDecimal rate;

    public ExchangeRate(Long id, CurrencyE baseCurrencyE, CurrencyE targetCurrencyE, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyE = baseCurrencyE;
        this.targetCurrencyE = targetCurrencyE;
        this.rate = rate;
    }

    public ExchangeRate(CurrencyE baseCurrencyE, CurrencyE targetCurrencyE, BigDecimal rate) {
        this.baseCurrencyE = baseCurrencyE;
        this.targetCurrencyE = targetCurrencyE;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyE getBaseCurrency() {
        return baseCurrencyE;
    }

    public void setBaseCurrency(CurrencyE baseCurrencyE) {
        this.baseCurrencyE = baseCurrencyE;
    }

    public CurrencyE getTargetCurrency() {
        return targetCurrencyE;
    }

    public void setTargetCurrency(CurrencyE targetCurrencyE) {
        this.targetCurrencyE = targetCurrencyE;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(id, that.id) && Objects.equals(baseCurrencyE, that.baseCurrencyE) && Objects.equals(targetCurrencyE, that.targetCurrencyE) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrencyE, targetCurrencyE, rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
               "id=" + id +
               ", baseCurrencyE=" + baseCurrencyE +
               ", targetCurrencyE=" + targetCurrencyE +
               ", rate=" + rate +
               '}';
    }
}
