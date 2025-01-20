package org.example.currencyexchangerefactoring.dao;

import org.example.currencyexchangerefactoring.model.Currency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.currencyexchangerefactoring.utils.Mapper.buildCurrency;


public class CurrencyDao implements Dao<Integer, Currency> {
    private static final StatementMaker STATEMENT_MAKER = new StatementMaker();
    private static final CurrencyDao INSTANCE = new CurrencyDao();
    private static final String SAVE_SQL = """
            INSERT INTO currencies (code, full_name, sign)
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE currencies
            SET code = ?,
                full_name = ?,
                sign = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                   code,
                   full_name,
                   sign
            FROM currencies
            """;
    private static final String FIND_BY_CODE_SQL = FIND_ALL_SQL + """
            WHERE code = ?
            """;


    @Override
    public List<Currency> findAll() {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(FIND_ALL_SQL);
            List<Currency> currencies = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> findByCode(String code) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(FIND_BY_CODE_SQL);
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;

            if (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Currency save(Currency currency) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                currency.setId((long) generatedKeys.getInt(1));
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Currency currency) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(UPDATE_SQL);
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.setLong(4, currency.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }
}
