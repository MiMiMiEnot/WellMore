package me.enot.wellgui.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.enot.wellgui.WellGUI;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class Settings {

    public List<String> getToPingServer() {
        return toPingServer;
    }

    public int getPingEvery() {
        return pingEvery;
    }

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
        if (createExampleConfig() && !(new File(GUI_DIR, "example.conf")).exists()) {
            try {
                Files.copy(getClass().getResourceAsStream("/example.conf"), GUI_DIR.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    private String permissionReloadConfigs;
    private String permissionReloadGuis;
    private String permissionReloadAll;

    private boolean hideAttributes;
    private boolean hideDestroys;
    private boolean hideEnchants;
    private boolean hidePlacedOn;
    private boolean hidePotionEffects;
    private boolean createExampleConfig;

    private int pingEvery;
    private List<String> toPingServer;

    private Object host;
    private int port;
    private String user;
    private String password;
    private String db;
    private void load(){
        permissionReloadAll = getSettings().getString("permissions.wellgui.reload-all");
        permissionReloadConfigs = getSettings().getString("permissions.wellgui.reload-configs");
        permissionReloadGuis = getSettings().getString("permissions.wellgui.reload-guis");
        hideAttributes = getSettings().getBoolean("item-flags.hide-attributes");
        hideDestroys = getSettings().getBoolean("item-flags.hide-destroys");
        hideEnchants = getSettings().getBoolean("item-flags.hide-enchants");
        hidePlacedOn = getSettings().getBoolean("item-flags.hide-placed-on");
        hidePotionEffects = getSettings().getBoolean("item-flags.hide-potion-effects");
        createExampleConfig = getSettings().getBoolean("create-example-gui");

        pingEvery = getSettings().getInt("ping.ping-every");
        toPingServer = getSettings().getStringList("ping.servers");

        host = settings.getAnyRef("mysql.host");
        port = settings.getInt("mysql.port");
        user = settings.getString("mysql.user");
        password = settings.getString("mysql.password");
        db = settings.getString("mysql.db");
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

    public boolean hideAttributes() {
        return hideAttributes;
    }

    public boolean hideDestroys() {
        return hideDestroys;
    }

    public boolean hideEnchants() {
        return hideEnchants;
    }

    public boolean hidePlacedOn() {
        return hidePlacedOn;
    }

    public boolean hidePotionEffects() {
        return hidePotionEffects;
    }

    public boolean createExampleConfig() {
        return createExampleConfig;
    }

    public Config getSettings() {
        return settings;
    }
    public Object getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDb() {
        return db;
    }
}
