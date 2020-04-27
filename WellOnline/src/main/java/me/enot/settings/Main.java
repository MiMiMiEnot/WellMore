package me.enot.settings;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import me.enot.WellOnline;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Main {
    private static Plugin plugin = WellOnline.getPlugin();
    private static Config main;
    private static File file = new File(plugin.getDataFolder(), "main.conf");
    private static Main mainConfig = new Main();

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        try {
            if(!file.exists()) {
                InputStream is = mainConfig.getClass().getResourceAsStream("/main.conf");
                if (is != null) {
                    Files.copy(is, file.toPath());
                }
            }
            main = ConfigFactory.parseFile(file).resolve();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Config getMain() {
        return main;
    }

    public static void reload(){
        if(file == null || main == null){
            create();
        }
        main = ConfigFactory.parseFile(file);
    }

    public static void save(){
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), main.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getLang(){
        return getMain().getString("language");
    }
}
