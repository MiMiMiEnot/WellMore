package me.enot.utils;

import me.enot.WellHide;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        for(String name : Enable.getEnabled()){
            Player p = Bukkit.getPlayer(name);
            if(p != null){
                 p.hidePlayer(WellHide.getPlugin(), e.getPlayer());
            }
        }
        if(Enable.getEnabled().contains(e.getPlayer().getName().toLowerCase())){
            for(Player p : Bukkit.getOnlinePlayers()){
                e.getPlayer().hidePlayer(WellHide.getPlugin(), p);
            }
        }
    }
}