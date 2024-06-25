package com.chenyue.mistplugin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    public String host;
    public int port;
    public String database;
    public String username;
    public String password;
    public static Connection connection;

    public MySQL(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", username, password);
        }
    }

    public static void disconnect() throws SQLException {
        if (isConnected()) {
            connection.close();
        }
    }

    public static boolean isConnected() {
        return (connection == null ? false : true);
    }

    public Connection getConnection() {
        return connection;
    }
}
