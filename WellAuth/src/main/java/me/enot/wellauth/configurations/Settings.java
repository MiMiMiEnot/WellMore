package me.enot.wellauth.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.enot.wellauth.WellAuth;
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

    private Plugin plugin = WellAuth.getPlugin();
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
        settings = ConfigFactory.parseFile(file);
        load();
    }

    private Object mysqlIp;
    private int mysqlPort;
    private Object mysqlUser;
    private Object mysqlPassword;

    private void load(){
        mysqlIp = getSettings().getAnyRef("mysql.ip");
        mysqlPort = getSettings().getInt("mysql.port");
        mysqlUser = getSettings().getAnyRef("mysql.user");
        mysqlPassword = getSettings().getAnyRef("mysql.password");
    }

    public Object getMysqlIp() {
        return mysqlIp;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public Object getMysqlPassword() {
        return mysqlPassword;
    }

    public Object getMysqlUser() {
        return mysqlUser;
    }

    public Config getSettings() {
        return settings;
    }

}
