package me.enot.utils;

import me.enot.mysql.Connect;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Enable {

    private static HashMap<String, Boolean> list = new HashMap<>();
    public static void load(){
        Connection connection = Connect.getConnection();
        String SQL = "SELECT * FROM `hide`";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()){
                String playername = rs.getString("player");
                boolean enabled = rs.getBoolean("hided");
                list.put(playername, enabled);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static ArrayList<String> enabled = new ArrayList<>();

    public static ArrayList<String> getEnabled(){
        enabled.clear();
        for(Map.Entry<String, Boolean> entry : list.entrySet()){
            if(entry.getValue()){
                enabled.add(entry.getKey());
            }
        }
        return enabled;
    }

}
