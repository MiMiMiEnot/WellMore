package me.enot.wellauthserver.events;

import me.enot.wellauthserver.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        boolean dc = Settings.getSettings().getBoolean("blockbreak.doesnt-cancel-if-has-permission");
        boolean cancel = Settings.getSettings().getBoolean("blockbreak.cancel");
        if(cancel){
            if(dc){
                String permission = Settings.getSettings().getString("blockbreak.permission");
                if(p.hasPermission(permission)){
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

}
