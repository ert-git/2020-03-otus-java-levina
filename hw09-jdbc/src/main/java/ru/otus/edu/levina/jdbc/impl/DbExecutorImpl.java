package ru.otus.edu.levina.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import ru.otus.edu.levina.jdbc.mapper.DbExecutor;

/**
 * @author sergey created on 03.02.19.
 */
@Slf4j
public class DbExecutorImpl<T> implements DbExecutor<T> {

    @Override
    public long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        log.debug("executeInsert: {}", sql);
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public Optional<T> executeSelect(Connection connection, String sql, long id,
            Function<ResultSet, T> rsHandler) throws SQLException {
        log.debug("executeSelect: {}", sql);
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    @Override
    public int executeUpdate(Connection connection, String sql, List<Object> params, Object id) throws SQLException {
        log.debug("executeUpdate: {}", sql);
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            // id
            pst.setObject(params.size() + 1, id);
            return pst.executeUpdate();
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}