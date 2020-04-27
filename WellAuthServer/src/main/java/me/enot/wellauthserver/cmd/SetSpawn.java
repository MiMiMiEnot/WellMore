package me.enot.wellauthserver.cmd;

import me.enot.wellauthserver.configs.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("setspawn")){
            if(s.hasPermission(Settings.getCmdPermission())){
                if(s instanceof Player){
                    Player p = (Player) s;
                    Location loc = p.getLocation();
                    String worldname = loc.getWorld().getName();
                    int x = loc.getBlockX();
                    int y = loc.getBlockY();
                    int z = loc.getBlockZ();

                    Settings.add("spawn-location.world", worldname);
                    Settings.add("spawn-location.X", x);
                    Settings.add("spawn-location.Y", y);
                    Settings.add("spawn-location.Z", z);

                    Settings.reload();
                    p.sendMessage(ChatColor.GREEN + "Успешно");
                }
            }
        }
        return false;
    }
}
