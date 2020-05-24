package me.enot.wellvanish.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.enot.wellvanish.WellVanish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Settings {

    protected static class SettingsHolder {
        public static final Settings INSTANCE = new Settings();
    }

    public static Settings getInstance(){
        return SettingsHolder.INSTANCE;
    }

    private Plugin plugin = WellVanish.getPlugin();
    private String fileName = "settings.conf";
    private File file = new File(plugin.getDataFolder(), fileName);
    private InputStream is = getClass().getResourceAsStream("/" + fileName);

    private Config settings = null;

    private void create(){
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        if(!file.exists()){
            try {
                Files.copy(is, file.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        settings = ConfigFactory.parseFile(file);
    }

    public void reload(){
        if(settings == null){
            create();
        }
        settings = ConfigFactory.parseFile(file).resolve();
        load();
    }

    private String permissionVanish;
    private String permissionVanishHistory;
    private String permissionVanishBypass;
    private void load(){
        permissionVanish = getSettings().getString("permissions.vanish");
        permissionVanishHistory = getSettings().getString("permissions.vanish-history");
        permissionVanishBypass = getSettings().getString("permissions.vanish-bypass");
    }

    public String getPermissionVanish() {
        return permissionVanish;
    }

    public String getPermissionVanishBypass() {
        return permissionVanishBypass;
    }

    public String getPermissionVanishHistory() {
        return permissionVanishHistory;
    }

    public Config getSettings() {
        return settings;
    }

}
