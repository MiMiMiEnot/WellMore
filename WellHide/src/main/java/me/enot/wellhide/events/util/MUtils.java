package me.enot.wellhide.events.util;

import me.enot.wellhide.WellHide;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MUtils {

    public static void hidePlayers(Player p) {
        Bukkit.getOnlinePlayers().forEach(player -> p.hidePlayer(WellHide.getPlugin(), player));
    }
    public static void showPlayers(Player p) {
        Bukkit.getOnlinePlayers().forEach(player -> p.hidePlayer(WellHide.getPlugin(), player));
    }
}
