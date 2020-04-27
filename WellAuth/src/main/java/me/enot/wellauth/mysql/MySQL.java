package me.enot.wellauth.mysql;

import me.enot.wellauth.configurations.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private static class MYSQL_HOLDER {
        public static final MySQL INSTANCE = new MySQL();
    }

    public static MySQL getInstance(){
        return MYSQL_HOLDER.INSTANCE;
    }

    private Connection connection;

    public void connect(){
        Object ip = Settings.getInstance().getMysqlIp();
        int port = Settings.getInstance().getMysqlPort();
        Object user = Settings.getInstance().getMysqlUser();
        Object password = Settings.getInstance().getMysqlPassword();
        String url = "jdbc:mysql://" + ip + ":" + port + "/auth?useSSL=false&autoReconnect=true";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                connection = DriverManager.getConnection(url, (String) user, (String) password);
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public void close(){
        try {
            if(!connection.isClosed())connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
