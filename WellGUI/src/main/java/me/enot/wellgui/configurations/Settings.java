package me.enot.wellgui.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.enot.wellgui.WellGUI;
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

    private Plugin plugin = WellGUI.getPlugin();
    private String fileName = "settings.conf";
    private File file = new File(plugin.getDataFolder(), fileName);
    private InputStream is = getClass().getResourceAsStream("/" + fileName);

    public final File GUI_DIR = new File(plugin.getDataFolder(), "/guis/");

    private Config settings = null;

    private void create(){
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        if(!GUI_DIR.exists()) GUI_DIR.mkdirs();
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


    private String permissionReloadConfigs;
    private String permissionReloadGuis;
    private String permissionReloadAll;
    private void load(){
        permissionReloadAll = getSettings().getString("permissions.wellgui.reload-all");
        permissionReloadConfigs = getSettings().getString("permissions.wellgui.reload-configs");
        permissionReloadGuis = getSettings().getString("permissions.wellgui.reload-guis");
    }

    public String getPermissionReloadAll() {
        return permissionReloadAll;
    }

    public String getPermissionReloadConfigs() {
        return permissionReloadConfigs;
    }

    public String getPermissionReloadGuis() {
        return permissionReloadGuis;
    }

    public Config getSettings() {
        return settings;
    }

}
