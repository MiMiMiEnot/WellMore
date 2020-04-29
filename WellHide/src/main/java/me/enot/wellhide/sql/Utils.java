package me.enot.wellhide.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

    public static final String SQL1 = "SELECT `hided` FROM `wellhide` WHERE `username` = ?";
    public static final String SQL2 = "INSERT INTO `wellhide`(`username`, `hided`) VALUES (?, ?)";
    public static final String SQL3 = "UPDATE `wellhide` SET `hided` = ? WHERE `username` = ?";

    public static boolean playersHided(String playerName){
        try {
            Connection connection = MySQL.getInstance().getConnection();
            PreparedStatement statements = connection.prepareStatement(SQL1);
            statements.setString(1, playerName.toLowerCase());
            ResultSet rs = statements.executeQuery();
            if (rs.next()){
                while (rs.next()){
                    return rs.getBoolean("hided");
                }
            } else {
                addPlayer(playerName, false);
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            MySQL.getInstance().close();
        }
        return false;
    }

    public static void addPlayer(String playerName, boolean hide){
        try {
            Connection connection = MySQL.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL2);
            statement.setString(1, playerName.toLowerCase());
            statement.setBoolean(2, hide);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            MySQL.getInstance().close();
        }
    }

    public static void update(String playerName, boolean to){
        try {
            Connection connection = MySQL.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL3);
            statement.setBoolean(1, to);
            statement.setString(2, playerName.toLowerCase());
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            MySQL.getInstance().close();
        }
    }

}
