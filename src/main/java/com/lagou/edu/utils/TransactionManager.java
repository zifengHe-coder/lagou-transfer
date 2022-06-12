package com.lagou.edu.utils;

import java.sql.SQLException;

public class TransactionManager {
//    private TransactionManager() {}

//    public static TransactionManager transactionManager = new TransactionManager();
//
//    public static TransactionManager getInstance() {
//        return transactionManager;
//    }
    private ConnectionUtils connectionUtils;

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
