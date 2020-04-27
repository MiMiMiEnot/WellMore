package me.enot.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import me.enot.WellHide;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class MySQL {
    private static Plugin plugin = WellHide.getPlugin();
    private static Config mysql;
    private static File file = new File(plugin.getDataFolder(), "mysql.conf");
    private static MySQL mySQL = new MySQL();

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        try {
            if(!file.exists()) {
                InputStream is = mySQL.getClass().getResourceAsStream("/mysql.conf");
                if (is != null) {
                    Files.copy(is, file.toPath());
                }
            }
            mysql = ConfigFactory.parseFile(file).resolve();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Config getMysql() {
        return mysql;
    }

    public static void reload(){
        if(file == null || mysql == null){
            create();
        }
        mysql = ConfigFactory.parseFile(file);
    }

    public static void save(){
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), mysql.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
