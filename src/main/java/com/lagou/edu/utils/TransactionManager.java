package com.lagou.edu.utils;

import java.sql.SQLException;

public class TransactionManager {
    public TransactionManager() {}

    public static TransactionManager transactionManager = new TransactionManager();

    public static TransactionManager getInstance() {
        return transactionManager;
    }


    public void beginTransaction() throws SQLException {
        System.out.println("开启事务");
        ConnectionUtils.getInstance().getCurrentThreadConn().setAutoCommit(false);
    }

    public void commit() throws SQLException {
        System.out.println("提交事务");
        ConnectionUtils.getInstance().getCurrentThreadConn().commit();
    }

    public void rollback() throws SQLException {
        System.out.println("事务回滚");
        ConnectionUtils.getInstance().getCurrentThreadConn().rollback();
    }
}
