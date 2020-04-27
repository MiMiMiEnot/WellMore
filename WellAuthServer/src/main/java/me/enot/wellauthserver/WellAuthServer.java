package me.enot.wellauthserver;

import me.enot.wellauthserver.cmd.SetSpawn;
import me.enot.wellauthserver.configs.Settings;
import me.enot.wellauthserver.events.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WellAuthServer extends JavaPlugin {
    public static Plugin getPlugin(){
        return WellAuthServer.getPlugin(WellAuthServer.class);
    }


    @Override
    public void onEnable(){
        Settings.reload();

        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new BlockBreak(), this);
        manager.registerEvents(new BlockIgnite(), this);
        manager.registerEvents(new BlockPlace(), this);
        manager.registerEvents(new EntityDamage(), this);
        manager.registerEvents(new Interact(), this);

        getCommand("setspawn").setExecutor(new SetSpawn());
    }

    @Override
    public void onDisable() {

    }
}
