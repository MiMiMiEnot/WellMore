package me.enot.mysql;

import me.enot.configurations.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    private static Object host = MySQL.getMysql().getAnyRef("host");
    private static int port = MySQL.getMysql().getInt("port");
    private static String user = MySQL.getMysql().getString("user");
    private static String password = MySQL.getMysql().getString("password");
    private static String db = MySQL.getMysql().getString("db");

    private static Connection connection;
    private static String SQL = "CREATE TABLE IF NOT EXISTS `hide` (`player` VARCHAR(32), `hided` INT(1))";
    private static void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, password);
                Statement st = connection.createStatement();
                st.execute(SQL);
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return connection;
    }

}
