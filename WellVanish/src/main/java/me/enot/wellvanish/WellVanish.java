package me.enot.wellvanish;

import me.enot.wellvanish.commands.WellVanishCMD;
import me.enot.wellvanish.configurations.Settings;
import me.enot.wellvanish.configurations.language.Language;
import me.enot.wellvanish.events.VanishEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WellVanish extends JavaPlugin {

    public static Plugin getPlugin() {
        return WellVanish.getPlugin(WellVanish.class);
    }

    @Override
    public void onEnable() {
        Settings.getInstance().reload();
        Language.getInstance().reload();
        getCommand("wellvanish").setExecutor(new WellVanishCMD());
        Bukkit.getPluginManager().registerEvents(new VanishEvents(), this);
    }

    @Override
    public void onDisable() {

    }
}
