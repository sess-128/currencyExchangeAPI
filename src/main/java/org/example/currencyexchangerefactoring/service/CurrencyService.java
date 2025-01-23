package org.example.currencyexchangerefactoring.service;

import org.example.currencyexchangerefactoring.dao.CurrencyDao;
import org.example.currencyexchangerefactoring.dto.CurrencyDto;
import org.example.currencyexchangerefactoring.model.CurrencyE;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    private CurrencyService() {
    }

    public List<CurrencyDto> findAll() {
        return currencyDao.findAll().stream()
                .map(currency -> new CurrencyDto(
                        currency.getId(), currency.getCode(), currency.getName(), currency.getSign()
                ))
                .collect(toList());
    }

    public Optional<CurrencyDto> findByCode(String code) {
        return currencyDao.findByCode(code).map(currencyE -> new CurrencyDto(
                currencyE.getId(), currencyE.getCode(), currencyE.getName(), currencyE.getSign()
        ));
    }

    public CurrencyDto save(CurrencyE currency) {
        CurrencyE save = currencyDao.save(currency);
        return new CurrencyDto(save.getId(), save.getCode(), save.getName(), save.getSign());
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
