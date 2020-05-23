package me.enot.wellonline.data.mysql;

import me.enot.wellonline.configurations.Settings;
import me.enot.wellonline.configurations.language.Langs;
import me.enot.wellonline.configurations.language.Replace;
import me.enot.wellonline.data.Utils;
import me.enot.wellonline.data.mysql.exception.MySQLDataConnectionFailed;
import me.enot.wellonline.utils.Message;
import org.bukkit.Bukkit;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MySQLHandler {

    protected static class Holder {
        public static final MySQLHandler INSTANCE = new MySQLHandler();
    }

    public static MySQLHandler getInstance() {
        return Holder.INSTANCE;
    }

    private Connection connection;

    private void connect() throws MySQLDataConnectionFailed {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try {
                    connection = DriverManager.getConnection(
                            "jdbc:mysql//" +
                                    Settings.getInstance().getHost() +
                                    ":" +
                                    Settings.getInstance().getPort() +
                                    "/" +
                                    Settings.getInstance().getDatabase() +
                                    "?useSSL=false&serverTimezone=UTC",
                    Settings.getInstance().getUsername(), Settings.getInstance().getPassword());
                } catch (SQLException e){
                    throw new MySQLDataConnectionFailed("Ошибка при инициализации подключения", e.getMessage());
                }
            } catch (ClassNotFoundException e) {
                throw new MySQLDataConnectionFailed("Класс драйвера не найден.", e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    connect();
                } catch (MySQLDataConnectionFailed e) {
                    Message.getInstance().sendMessage(
                            Bukkit.getConsoleSender(),
                            Langs.error__data__mysql__error_while_connect,
                            new Replace("\\{E\\}", e.getMessage())
                    );
                }
            }
        } catch (SQLException e) {e.printStackTrace();}
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    public void log(String username, long joinTime, long quitTime) {
        log(username, (quitTime - joinTime));
    }

    public void log(String username, long onlineTime) {
        long millis = System.currentTimeMillis();
        Connection connection = getConnection();
        try {
            String SQL = "INSERT INTO `online` (`username`, `online-time`, `date`, `server`) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, username.toLowerCase());
            ps.setLong(2, onlineTime);
            ps.setLong(3, millis);
            ps.setString(4, Settings.getInstance().getServer());
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Long getOnlineTime(String playerName, long date) {
        return getOnlineTime(playerName, Utils.convertToDayStart(date), Utils.convertToDayEnd(date), Settings.getInstance().getServer());
    }

    public Long getOnlineTime(String playerName, long date, String server) {
        return getOnlineTime(playerName, Utils.convertToDayStart(date), Utils.convertToDayEnd(date), server);
    }

    public Long getOnlineTime(String playerName, long startDate, long endDate, String server) {
        try {
            Connection connection = getConnection();
            String SQL = "SELECT `online-time` FROM `online` WHERE `username` = ? AND `server` = ? AND `online-time` BETWEEN ? AND ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, playerName.toLowerCase());
            statement.setString(2, server);
            statement.setLong(3, startDate);
            statement.setLong(4, endDate);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Long onlineTime = 0L;
                while (rs.next()) {
                    onlineTime += rs.getLong("online-time");
                }
                return onlineTime;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }


}
