package com.lagou.edu.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 饿汉式实例化
 */
@Component("connectionUtils")
public class ConnectionUtils {
    @Autowired
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

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
            connection = dataSource.getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }
}
