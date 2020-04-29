package me.enot.wellhide;

import me.enot.wellhide.configurations.Settings;
import me.enot.wellhide.configurations.language.Language;
import me.enot.wellhide.sql.MySQL;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WellHide extends JavaPlugin {

    public static Plugin getPlugin() {
        return WellHide.getPlugin(WellHide.class);
    }

    @Override
    public void onEnable() {
        Settings.getInstance().reload();
        Language.getInstance().reload();
    }

    @Override
    public void onDisable() {
        MySQL.getInstance().close();
    }
}
