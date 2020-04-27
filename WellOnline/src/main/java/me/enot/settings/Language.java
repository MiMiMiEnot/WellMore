package me.enot.settings;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import me.enot.WellOnline;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Language {
    private static Plugin plugin = WellOnline.getPlugin();
    private static Config language;
    private static File file = new File(plugin.getDataFolder(), "language.conf");
    private static Language languageclass = new Language();

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdirs();
        }
        try {
            if(!file.exists()){
                InputStream is = languageclass.getClass().getResourceAsStream("/language.conf");
                if(is != null){
                    Files.copy(is, file.toPath());
                }
            }
            language = ConfigFactory.parseFile(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Config getLanguage(){
        return language;
    }

    public  static void reload(){
        if(file == null || language == null){
            create();
        }
        language = ConfigFactory.parseFile(file);
    }

    public static void save(){
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), language.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String get(String path){
        return toString(getLanguage().getString(path));
    }

    private static String toString(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
