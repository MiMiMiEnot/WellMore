package me.enot.wellonline;

import me.enot.wellonline.commands.WellOnlineCMD;
import me.enot.wellonline.configurations.Settings;
import me.enot.wellonline.configurations.language.Language;
import me.enot.wellonline.events.Events;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WellOnline extends JavaPlugin {

    public static Plugin getPlugin() {
        return WellOnline.getPlugin(WellOnline.class);
    }


    @Override
    public void onEnable() {
        Settings.getInstance().reload();
        Language.getInstance().reload();
        getCommand("wellonline").setExecutor(new WellOnlineCMD());
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable(){

    }


}
