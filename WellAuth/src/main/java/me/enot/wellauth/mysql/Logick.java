package me.enot.wellauth.mysql;


import me.enot.wellauth.WellAuth;
import me.enot.wellauth.commands.WellAuthCMD;
import me.enot.wellauth.configurations.language.Langs;
import me.enot.wellauth.configurations.language.Replace;
import me.enot.wellauth.utils.AuthAccount;
import me.enot.wellauth.utils.Message;
import me.enot.wellauth.utils.Result;
import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Logick {

    public static boolean isLinked(String username){
        int id = getID(username);
        if(id != -1){
            Connection connection = MySQL.getInstance().getConnection();
            try {
                String SQL = "SELECT * FROM `linked_accounts` WHERE `accountid` = ?";
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    return true;
                }
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                MySQL.getInstance().close();
            }
        }
        return false;
    }

    public static List<String> createHistory(String username, int limit, Result result){
        int id = getID(username);
        if(id != -1){
            try {
                Connection connection = MySQL.getInstance().getConnection();
                List<String> list = new ArrayList<>();
                String SQL = "SELECT * FROM `game_login_attempts` WHERE `account_id` = ? ORDER BY `id` ASC LIMIT ?";
                if(result != null) SQL = "SELECT * FROM `game_login_attempts` WHERE `account_id` = ? AND `result` = '" + result.name().toUpperCase() + "' ORDER BY `id` ASC LIMIT ?";
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setInt(1, id);
                statement.setInt(2, limit+1);
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    list.add(Message.getInstance().getMessage(Langs.account__attempts_start,
                            new Replace("\\{name\\}", username),
                            new Replace("\\{limit\\}", Integer.toString(limit))));
                    while (rs.next()){
                        Timestamp timestamp = rs.getTimestamp("time");
                        String ip = getIP(rs.getLong("ip"));
                        String res = rs.getString("result");
                        list.add(Message.getInstance().getMessage(Langs.account__attempts_history_entry,
                                new Replace("\\{date\\}", timestamp.toString()),
                                new Replace("\\{ip\\}", ip),
                                new Replace("\\{type\\}", res)));
                    }
                }
                return list;
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                MySQL.getInstance().close();
            }
        }
        return null;
    }

    public static boolean accountExists(String username){
        Connection connection = MySQL.getInstance().getConnection();
        try {
            String SQL = "SELECT * FROM `accounts` WHERE `username` = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, username.toLowerCase());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            MySQL.getInstance().close();
        }
        return false;
    }

    public static int getID(String username){
        if(accountExists(username)){
            Connection connection = MySQL.getInstance().getConnection();
            try {
                String SQL = "SELECT `id` FROM `accounts` WHERE `username` = ?";
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) return rs.getInt("id");
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                MySQL.getInstance().close();
            }
        }
        return -1;
    }
    public static String getUsername(String id){
            try {
                Connection connection = MySQL.getInstance().getConnection();
                String SQL = "SELECT `username` FROM `accounts` WHERE `id` = ?";
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setInt(1, Integer.parseInt(id));
                ResultSet rs = statement.executeQuery();
                if (rs.next()) return rs.getString("username");
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                MySQL.getInstance().close();
            }
        return null;
    }

    public static AuthAccount getAccountInfo(String name){
        if(accountExists(name)) {
            try {
                Connection connection = MySQL.getInstance().getConnection();
                String SQL = "SELECT * FROM `accounts` WHERE `username` = ?";
                PreparedStatement ps = connection.prepareStatement(SQL);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    int id = rs.getInt("id");
                    String regTime = rs.getString("reg_time");
                    long regIp = rs.getLong("reg_ip");
                    boolean loggedIn = rs.getBoolean("logged_in");
                    Long sessionIp = rs.getLong("session_ip");
                    Long sessionEndTime = rs.getLong("session_end_time");
                    boolean blocked = rs.getBoolean("banned");
                    Long whitelistedIp = rs.getLong("whitelist_ip");
                    String totpKey = rs.getString("TOTP_key");
                    boolean totpEnabled = rs.getBoolean("TOTP_disabled");
                    return new AuthAccount(id, regTime, regIp, loggedIn, sessionIp, sessionEndTime,
                            blocked, whitelistedIp, totpKey, totpEnabled);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MySQL.getInstance().close();
            }
        }
        return null;
    }

    public static List<String> dupeipHistory(String arg) {
        String SQL;
        String set;
        if (WellAuthCMD.validIP(arg)) {
            SQL = "SELECT `account_id` FROM `game_login_attempts` WHERE `ip` = ?";
            Bukkit.getConsoleSender().sendMessage(arg);
            set = getIP(arg);
            Bukkit.getConsoleSender().sendMessage(set);
        } else {
            SQL = "SELECT `account_id` FROM `game_login_attempts` WHERE `ip` IN (SELECT `ip` FROM `game_login_attempts` WHERE `account_id` = ?)";
            set = Integer.toString(getID(arg));
        }
        try {
            Connection connection = MySQL.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, set);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                List<String> listId = new ArrayList<>();
                while (rs.next()){
                    String account_id = Integer.toString(rs.getInt("account_id"));
                    if (!listId.contains(account_id)){
                        listId.add(account_id);
                    }
                }
                List<String> list = new ArrayList<>();
                listId.forEach(string -> list.add(getUsername(string)));
                return list;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            MySQL.getInstance().close();
        }
        return null;
    }

    public static String getIP(long l) {
        try {
            return InetAddress.getByName(Long.toString(l)).getHostAddress();
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        return "";
    }
    public static String getIP(String s) {
        try {
            return Integer.toString(ByteBuffer.wrap(InetAddress.getByName(s).getAddress()).getInt());
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        return "";
    }

}
