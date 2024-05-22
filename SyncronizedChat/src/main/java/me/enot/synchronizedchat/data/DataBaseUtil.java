package me.enot.synchronizedchat.data;

import me.enot.synchronizedchat.chat.obj.ChatPlayer;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class DataBaseUtil {

    public static boolean isLinked(MySQL mySQL, String username) {
        Connection connection = null;
        try {
            connection = mySQL.getConnection();
            String SQL = "SELECT * FROM auth.vk_link WHERE account_id IN (SELECT id FROM auth.accounts WHERE username = ?)";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            boolean resnex = rs.next();
            return resnex;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    public static Set<String> ignoreList(MySQL mySQL, String username) {
        Connection connection = null;
        try {
            connection = mySQL.getConnection();
            String SQL = "select * from Chat.ignore WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new HashSet<>(Arrays.asList(rs.getString(1).split(",")));
//                return list;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void ignoreChange(MySQL mySQL, ChatPlayer chatPlayer) {
        Connection connection = null;
        try {
            connection = mySQL.getConnection();
            String SQL = "INSERT INTO Chat.ignore(username,ignored) VALUES (?,?) ON DUPLICATE KEY UPDATE ignored = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
//            chatPlayer.getIgnored().remove(chatPlayer.getPlayerName());
            String j = String.join(",", chatPlayer.getIgnored());
            statement.setString(1, chatPlayer.getPlayerName());
            statement.setString(2, j);
            statement.setString(3, j);
//            Bukkit.getConsoleSender().sendMessage(statement.toString());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

}