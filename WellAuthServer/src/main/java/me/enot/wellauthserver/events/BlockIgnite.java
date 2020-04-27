package me.enot.wellauthserver.events;

import me.enot.wellauthserver.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class BlockIgnite implements Listener {


    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e){
        Player p = e.getPlayer();
        boolean dc = Settings.getSettings().getBoolean("blockignite.doesnt-cancel-if-has-permission");
        boolean cancel = Settings.getSettings().getBoolean("blockignite.cancel");
        if(cancel){
            if(dc){
                String permission = Settings.getSettings().getString("blockignite.permission");
                if(p.hasPermission(permission)){
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

}
