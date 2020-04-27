package me.enot.wellauth;

import me.enot.wellauth.commands.WellAuthCMD;
import me.enot.wellauth.configurations.Settings;
import me.enot.wellauth.configurations.language.Language;
import me.enot.wellauth.mysql.MySQL;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WellAuth extends JavaPlugin {

    public static Plugin getPlugin(){
        return WellAuth.getPlugin(WellAuth.class);
    }

    @Override
    public void onEnable() {
        Settings.getInstance().reload();
        Language.getInstance().reload();

        getCommand("wellauth").setExecutor(new WellAuthCMD());

        MySQL.getInstance().connect();
    }

    @Override
    public void onDisable() {
        MySQL.getInstance().close();
    }
}
