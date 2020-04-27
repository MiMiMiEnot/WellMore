package me.enot.wellauthserver.events;

import me.enot.wellauthserver.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        World world = Bukkit.getWorld(Settings.getSettings().getString("spawn-location.world"));
        int x = Settings.getSettings().getInt("spawn-location.X");
        int y = Settings.getSettings().getInt("spawn-location.Y");
        int z = Settings.getSettings().getInt("spawn-location.Z");
        Location location = new Location(world, x, y, z);
        p.teleport(location);
    }

}
