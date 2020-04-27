package me.enot;

import me.enot.utils.Enable;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WellHide extends JavaPlugin {
    public static Plugin getPlugin(){
        return WellHide.getPlugin(WellHide.class);
    }

    @Override
    public void onEnable() {
        Enable.load();
    }

    @Override
    public void onDisable() {

    }
}
