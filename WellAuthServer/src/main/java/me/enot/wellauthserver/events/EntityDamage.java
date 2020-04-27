package me.enot.wellauthserver.events;

import me.enot.wellauthserver.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamage implements Listener {


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            boolean dc = Settings.getSettings().getBoolean("entitydamage.doesnt-cancel-if-has-permission");
            boolean cancel = Settings.getSettings().getBoolean("entitydamage.cancel");
            if (cancel) {
                if (dc) {
                    String permission = Settings.getSettings().getString("entitydamage.permission");
                    if (p.hasPermission(permission)) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

}
