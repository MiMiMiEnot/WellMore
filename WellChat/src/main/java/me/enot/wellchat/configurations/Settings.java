package me.enot.wellchat.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.enot.wellchat.WellChat;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class Settings {

    protected static class SettingsHolder {
        public static final Settings INSTANCE = new Settings();
    }

    public static Settings getInstance(){
        return SettingsHolder.INSTANCE;
    }

    private Plugin plugin = WellChat.getPlugin();
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

    private int localChatRadius;
    private boolean localChatEnabled;
    private List<String> blockedWords;
    private List<String> replaceTo;
    private String permissionSeeOriginal;
    private String permissionSpy;
    private String permissionWellChatReload;
    private boolean antiMatEnabled;
    private void load (){
        localChatEnabled = settings.getBoolean("local-chat.enabled");
        localChatRadius = settings.getInt("local-chat.radius");
        blockedWords = settings.getStringList("blocked");
        replaceTo = settings.getStringList("replace-to");
        permissionSeeOriginal = settings.getString("permissions.see-original");
        permissionSpy = settings.getString("permissions.spy");
        permissionWellChatReload = settings.getString("permissions.wellchat.reload");
        antiMatEnabled = settings.getBoolean("anti-mat-enabled");
    }

    public int getLocalChatRadius() {
        return localChatRadius;
    }

    public boolean isLocalChatEnabled() {
        return localChatEnabled;
    }

    public List<String> getBlockedWords() {
        return blockedWords;
    }

    public List<String> getReplaceTo() {
        return replaceTo;
    }

    public String getPermissionSeeOriginal() {
        return permissionSeeOriginal;
    }

    public String getPermissionSpy() {
        return permissionSpy;
    }

    public String getPermissionWellChatReload() {
        return permissionWellChatReload;
    }

    public boolean isAntiMatEnabled() {
        return antiMatEnabled;
    }
}
