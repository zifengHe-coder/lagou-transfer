package com.lagou.edu.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component("transactionManager")
public class TransactionManager {
//    private TransactionManager() {}

//    public static TransactionManager transactionManager = new TransactionManager();
//
//    public static TransactionManager getInstance() {
//        return transactionManager;
//    }
    @Autowired
    private ConnectionUtils connectionUtils;

    public ConnectionUtils getConnectionUtils() {
        return connectionUtils;
    }

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    public void beginTransaction() throws SQLException {
        System.out.println("开启事务");
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }

    public void commit() throws SQLException {
        System.out.println("提交事务");
        connectionUtils.getCurrentThreadConn().commit();
    }

    public void rollback() throws SQLException {
        System.out.println("事务回滚");
        connectionUtils.getCurrentThreadConn().rollback();
    }
}
