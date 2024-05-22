package me.enot.wellgui.gui.guiitem.utils;

import me.enot.wellgui.WellGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerPingerEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
//        Bukkit.getConsoleSender().sendMessage(Bukkit.getOnlinePlayers().size() + "");
        Bukkit.getScheduler().runTaskLater(WellGUI.getPlugin(), () -> {
            if (Bukkit.getOnlinePlayers().size() == 1) {
                WellGUI.getPinger().restart();
            }
        }, 40);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
//        Bukkit.getConsoleSender().sendMessage("" + Bukkit.getOnlinePlayers().size());
        if (Bukkit.getOnlinePlayers().size() < 2) {
            WellGUI.getPinger().stop();
        }
    }

}
