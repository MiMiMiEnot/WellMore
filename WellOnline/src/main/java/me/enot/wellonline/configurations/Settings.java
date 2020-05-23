package me.enot.wellonline.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.enot.wellonline.WellOnline;
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

    private Plugin plugin = WellOnline.getPlugin();
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

    public Config getSettings() {
        return settings;
    }

    private boolean loggingEnabled;

    private Object host;
    private int port;
    private String database;
    private String username;
    private String password;

    private String server;

    private String wellOnlinePermisison;

    private void load() {
        loggingEnabled = getSettings().getBoolean("data.enable-data-logging");

        host = getSettings().getAnyRef("data.mysql.host");
        port = getSettings().getInt("data.mysql.port");
        database = getSettings().getString("data.mysql.database");
        username = getSettings().getString("data.mysql.user");
        password = getSettings().getString("data.mysql.password");
        server = getSettings().getString("server");

        wellOnlinePermisison = getSettings().getString("permissions.wellonline-cmd");
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public Object getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getServer() {
        return server;
    }

    public String getWellOnlinePermisison() {
        return wellOnlinePermisison;
    }
}
