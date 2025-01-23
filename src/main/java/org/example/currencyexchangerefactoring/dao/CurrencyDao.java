package org.example.currencyexchangerefactoring.dao;

import org.example.currencyexchangerefactoring.exception.DaoException;
import org.example.currencyexchangerefactoring.model.CurrencyE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.currencyexchangerefactoring.utils.Mapper.buildCurrency;


public class CurrencyDao implements Dao<Integer, CurrencyE> {
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
    public List<CurrencyE> findAll() {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(FIND_ALL_SQL);
            List<CurrencyE> currencies = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<CurrencyE> findByCode(String code) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(FIND_BY_CODE_SQL);
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            CurrencyE currencyE = null;

            if (resultSet.next()) {
                currencyE = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currencyE);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    @Override
    public CurrencyE save(CurrencyE currencyE) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, currencyE.getCode());
            preparedStatement.setString(2, currencyE.getName());
            preparedStatement.setString(3, currencyE.getSign());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                currencyE.setId((long) generatedKeys.getInt(1));
            }
            return currencyE;
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public void update(CurrencyE currencyE) {
        try {
            PreparedStatement preparedStatement = STATEMENT_MAKER.getStatement(UPDATE_SQL);
            preparedStatement.setString(1, currencyE.getCode());
            preparedStatement.setString(2, currencyE.getName());
            preparedStatement.setString(3, currencyE.getSign());
            preparedStatement.setLong(4, currencyE.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }
}
