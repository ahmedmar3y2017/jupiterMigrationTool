package com.asset.jupiter.Util.ApplicationEvents;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Deny Prasetyo
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;


    private DatabaseConnection(String driver, String url, String username, String password) throws SQLException, ClassNotFoundException {

            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, username, password);

    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance(String driver, String url, String username, String password) throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DatabaseConnection(driver, url, username, password);
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection(driver, url, username, password);
        }

        return instance;
    }
}