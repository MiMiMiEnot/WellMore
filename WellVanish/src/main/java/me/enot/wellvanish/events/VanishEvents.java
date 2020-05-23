package me.enot.wellvanish.events;

import me.enot.wellvanish.WellVanish;
import me.enot.wellvanish.configurations.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VanishEvents implements Listener {

    public static List<String> vanishMap = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission(Settings.getInstance().getPermissionVanishBypass())) {
            vanishMap.forEach(s -> {
                Player player = Bukkit.getPlayer(s);
                if (player != null) p.hidePlayer(WellVanish.getPlugin(), player);
            });
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        String playerName = e.getPlayer().getName();
        vanishMap.remove(playerName);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(WellVanish.getPlugin(), e.getPlayer());
        }
    }

}
