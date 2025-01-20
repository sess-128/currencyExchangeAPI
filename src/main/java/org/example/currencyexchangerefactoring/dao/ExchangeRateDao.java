package org.example.currencyexchangerefactoring.dao;

import org.example.currencyexchangerefactoring.exception.DaoException;
import org.example.currencyexchangerefactoring.exception.ExchangeRateAlreadyExistException;
import org.example.currencyexchangerefactoring.model.ExchangeRate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.currencyexchangerefactoring.utils.Mapper.buildExchangeRate;

public class ExchangeRateDao implements Dao<Integer, ExchangeRate> {
    private static final StatementMaker STATEMENT_MAKER = new StatementMaker();
    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();
    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates (
                base_currency_id,
                target_currency_id,
                rate)
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
                UPDATE exchange_rates
                SET rate = ?
                WHERE base_currency_id = ? AND target_currency_id = ?
            """;
    private static final String FIND_ALL_WITH_CURRENCIES_SQL = """
        
            SELECT
            er.id AS exchange_rate_id,
            er.base_currency_id,
            bc.code AS base_currency_code,
            bc.full_name AS base_currency_full_name,
            bc.sign AS base_currency_sign,
            er.target_currency_id,
            tc.code AS target_currency_code,
            tc.full_name AS target_currency_full_name,
            tc.sign AS target_currency_sign,
            er.rate
        FROM
            exchange_rates er
        JOIN
            currencies bc ON er.base_currency_id = bc.id
        JOIN
            currencies tc ON er.target_currency_id = tc.id
        """;
    private static final String FIND_BY_PAIR_SQL = FIND_ALL_WITH_CURRENCIES_SQL + """
            WHERE
                         er.base_currency_id = ?
                         AND er.target_currency_id = ?
            """;

    private ExchangeRateDao() {
    }

    @Override
    public List<ExchangeRate> findAll() {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(FIND_ALL_WITH_CURRENCIES_SQL);
            List<ExchangeRate> exchangeRates = new ArrayList<>();

            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<ExchangeRate> findByPair(String base, String target) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(FIND_BY_PAIR_SQL);
            preparedStatement.setString(1, base);
            preparedStatement.setString(2, target);

            var resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;

            if (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }

            return Optional.ofNullable(exchangeRate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setLong(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                exchangeRate.setId(generatedKeys.getLong(1));
            }

            return exchangeRate;

        } catch (SQLException e) {
            throw new ExchangeRateAlreadyExistException(e);
        }
    }

    @Override
    public void update(ExchangeRate exchangeRate) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(UPDATE_SQL);
            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setObject(2, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setObject(3, exchangeRate.getTargetCurrency().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    }
