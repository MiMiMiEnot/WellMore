package me.enot.wellhide.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        PlayerJoin.getActiveAccounts().forEach(ac -> {
            if (ac.getPlayerName().equalsIgnoreCase(p.getName())) PlayerJoin.getActiveAccounts().remove(ac);
        });
    }
}
