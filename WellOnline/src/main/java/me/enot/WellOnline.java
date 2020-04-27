package me.enot;

import me.enot.events.Events;
import me.enot.events.commands.Check;
import me.enot.events.commands.WellOnlineCMD;
import me.enot.settings.Language;
import me.enot.settings.Main;
import me.enot.utils.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WellOnline extends JavaPlugin {

    private static Plugin plugin;


    private static boolean mysql;

    @Override
    public void onEnable() {
        plugin = this;
        Main.reload();
        Language.reload();
        mysql = Main.getMain().getBoolean("mysql-enabled");
        if (Main.getMain().getBoolean("logging")) {
            Bukkit.getPluginManager().registerEvents(new Events(), this);
        } else {
            Bukkit.getConsoleSender().sendMessage(Language.get("logging-disabled_" + Main.getLang()));
        }
        getCommand("check").setExecutor(new Check());
        getCommand("wellonline").setExecutor(new WellOnlineCMD());
        if (isMysql()) {
            MySQL.connect();
        }
    }

    public static boolean isMysql() {
        return mysql;
    }

    @Override
    public void onDisable(){
        MySQL.close();
    }


    public static Plugin getPlugin() {
        return plugin;
    }

}
