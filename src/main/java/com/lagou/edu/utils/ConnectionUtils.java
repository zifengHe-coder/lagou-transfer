package com.lagou.edu.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 饿汉式实例化
 */
public class ConnectionUtils {
//    private ConnectionUtils() {
//    }
//
//    private static ConnectionUtils connectionUtils = new ConnectionUtils();
//
//    public static ConnectionUtils getInstance() {
//        return connectionUtils;
//    }

    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();//当前线程连接

    public Connection getCurrentThreadConn() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = DruidUtils.getInstance().getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }
}
