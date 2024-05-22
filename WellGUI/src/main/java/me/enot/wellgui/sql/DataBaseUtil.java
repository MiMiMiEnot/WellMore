package me.enot.wellgui.sql;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DataBaseUtil {


    public static boolean isLinked(Connection connection, String username) {
        final String SQL = "select vk_id from vk_link where account_id in (select id from accounts where username = ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}