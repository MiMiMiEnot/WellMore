package me.enot.wellauthserver.events;

import me.enot.wellauthserver.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        boolean dc = Settings.getSettings().getBoolean("blockplace.doesnt-cancel-if-has-permission");
        boolean cancel = Settings.getSettings().getBoolean("blockplace.cancel");
        if(cancel){
            if(dc){
                String permission = Settings.getSettings().getString("blockplace.permission");
                if(p.hasPermission(permission)){
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

}
