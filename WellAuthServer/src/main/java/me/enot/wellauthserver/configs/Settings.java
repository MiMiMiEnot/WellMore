package me.enot.wellauthserver.configs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import me.enot.wellauthserver.WellAuthServer;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
public class Settings {

    private static Plugin plugin = WellAuthServer.getPlugin();
    private static Config settingscnf;
    private static final File file = new File(plugin.getDataFolder(), "settings.conf");
    private static Settings settings = new Settings();

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        try {
            if(!file.exists()) {
                InputStream is = settings.getClass().getResourceAsStream("/settings.conf");
                if (is != null) {
                    Files.copy(is, file.toPath());
                }
            }
            settingscnf = ConfigFactory.parseFile(file).resolve();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Config getSettings() {
        return settingscnf;
    }



    private static String cmdPermission;
    public static void reload() {
        if (file == null || settingscnf == null) {
            create();
        }
        settingscnf = ConfigFactory.parseFile(file);
        cmdPermission = getSettings().getString("command-set-spawn.permission");
    }

    public static String getCmdPermission() {
        return cmdPermission;
    }

    private static void save(){
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), settingscnf.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void add(String path, Object value){
        settingscnf = settingscnf.withValue(path, ConfigValueFactory.fromAnyRef(value));
        save();
    }
    public static void remove(String path){
        settingscnf = settingscnf.withoutPath(path);
        save();
    }

}
