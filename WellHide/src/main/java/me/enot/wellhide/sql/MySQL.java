package me.enot.wellhide.sql;

import me.enot.wellhide.configurations.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private MySQL(){}
    protected static class MySQL_HOLDER {
        public static final MySQL INSTANCE = new MySQL();
    }

    public static MySQL getInstance() {
        return MySQL_HOLDER.INSTANCE;
    }

    private Connection connection;

    public void connect () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
                        Settings.getInstance().getHost(),
                        Settings.getInstance().getPort(),
                        Settings.getInstance().getDb());
                connection = DriverManager.getConnection(url, Settings.getInstance().getUser(), Settings.getInstance().getPassword());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public synchronized Connection getConnection() {
        if (connection == null) connect();
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e){}
    }
}
