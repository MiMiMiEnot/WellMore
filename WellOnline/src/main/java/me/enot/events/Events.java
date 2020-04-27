package me.enot.events;

import me.enot.utils.MySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class Events implements Listener {

    private static HashMap<String, Long> temp = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        temp.put(e.getPlayer().getName(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Long time = temp.get(e.getPlayer().getName());
        temp.remove(e.getPlayer().getName());
        MySQL.setQuit(e.getPlayer().getName(), System.currentTimeMillis() - time);
    }
}
