package me.enot.wellgui.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.enot.wellgui.configurations.Settings;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;
    private Connection connection;

    public MySQL() {

        config.setJdbcUrl("jdbc:mysql://" + Settings.getInstance().getHost() + ":" + Settings.getInstance().getPort() + "/" + Settings.getInstance().getDb());
        config.setUsername(Settings.getInstance().getUser());
        config.setPassword(Settings.getInstance().getPassword());
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPoolName("WellGUI");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("serverTimezone", "UTC");
        config.setMaximumPoolSize(2);
//        config.setIdleTimeout(30000);
        config.setLeakDetectionThreshold(60000);
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    connection = ds.getConnection();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public void close() {
        try {
            getConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}