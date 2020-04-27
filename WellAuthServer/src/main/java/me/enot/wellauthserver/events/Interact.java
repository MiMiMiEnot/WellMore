package me.enot.wellauthserver.events;

import me.enot.wellauthserver.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {


    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        boolean dc = Settings.getSettings().getBoolean("interact.doesnt-cancel-if-has-permission");
        boolean cancel = Settings.getSettings().getBoolean("interact.cancel");
        if(cancel){
            if(dc){
                String permission = Settings.getSettings().getString("interact.permission");
                if(p.hasPermission(permission)){
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

}
