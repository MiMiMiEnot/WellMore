package me.enot.wellonline.events;

import me.enot.wellonline.configurations.Settings;
import me.enot.wellonline.data.mysql.MySQLHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class Events implements Listener {


    public static HashMap<String, Long> map = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e){
        if (Settings.getInstance().isLoggingEnabled()) {
            if (!map.containsKey(e.getPlayer().getName().toLowerCase())) {
                map.put(e.getPlayer().getName().toLowerCase(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerQuitEvent e){
        if (Settings.getInstance().isLoggingEnabled()) {
            String playerName = e.getPlayer().getName().toLowerCase();
            Long joinTime = map.get(playerName);
            MySQLHandler.getInstance().log(playerName, joinTime, System.currentTimeMillis());

            map.remove(playerName, joinTime);
        }
    }

}
