package ru.pukhov.shop.db;

import ru.pukhov.shop.exception.DataBaseDriverLoadException;
import ru.pukhov.shop.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager {
    private static final String DRIVER_CLASS = "db.driver-class";
    private static final String URL = "db.url";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";
    private static ConnectionManager instance;

    public ConnectionManagerImpl() {
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerImpl();
            loadDriver(PropertiesUtil.getProperties(DRIVER_CLASS));
        }
        return instance;
    }

    private static void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new DataBaseDriverLoadException("Database driver not loaded.");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PropertiesUtil.getProperties(URL),
                PropertiesUtil.getProperties(USERNAME),
                PropertiesUtil.getProperties(PASSWORD)
        );
    }
}
