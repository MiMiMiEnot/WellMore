package me.enot.utils;

import me.enot.events.utils.User;
import me.enot.settings.Language;
import me.enot.settings.Main;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;

public class MySQL {

    private static Connection connection = null;

    private static Object host = Main.getMain().getAnyRef("mysql.host");
    private static int port = Main.getMain().getInt("mysql.port");
    private static String user = Main.getMain().getString("mysql.user");
    private static String password = Main.getMain().getString("mysql.password");
    private static String db = Main.getMain().getString("mysql.db");
    public static void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, password);
                Statement st = connection.createStatement();
                String SQL = "CREATE TABLE IF NOT EXISTS `online`(" +
                        "`id` INT AUTO_INCREMENT," +
                        "`username` VARCHAR(65) NOT NULL," +
                        "`time` INT NOT NULL," +
                        "`date` BIGINT NOT NULL," +
                        "`server` VARCHAR(32) NOT NULL," +
                        "PRIMARY KEY(`id`))";
                st.execute(SQL);
            } catch (SQLException e){
                Bukkit.getConsoleSender().sendMessage(Language.get("error_" + Main.getLang()).replace("{ERROR}", e.getMessage()));
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        if(connection == null){
            connect();
        }
        return connection;
    }
    public static void close(){
        try {
            getConnection().close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void setQuit(String nick, Long time){
        String SQL = "INSERT INTO `online` (`username`, `time`, `date`, `server`) VALUES ('" + nick.toLowerCase() + "', " + time + ", " +
                System.currentTimeMillis() + ", '" + Main.getMain().getString("server-name") + "')";
        try {
            Statement st = getConnection().createStatement();
            st.execute(SQL);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static ArrayList<User> getUserListByNum(int count, String username, String server){
        ArrayList<User> userlist = new ArrayList<>();
        String SQL = "SELECT * FROM `online` WHERE `username` = '" + username.toLowerCase() + "' AND `server` = '" + server + "' ORDER BY `date` DESC ";
        try {
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            int s = 0;
            if (rs.next()) {
                while (rs.next()) {
                    if (count > s)
                        userlist.add(new User(username, rs.getLong("time"), rs.getLong("date"), server));
                    s++;
                }
            } else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return userlist;
    }
}
