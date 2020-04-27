package me.enot.wellgui.configurations.language;

import com.typesafe.config.*;
import me.enot.wellgui.WellGUI;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


public class Language {

    protected static class LanguageHolder {
        public static final Language INSTANCE = new Language();
    }

    public static Language getInstance(){
        return LanguageHolder.INSTANCE;
    }

    private Plugin plugin = WellGUI.getPlugin();
    private String fileName = "language.conf";
    private File file = new File(plugin.getDataFolder(), fileName);

    private Config language = null;

    private void create(){
        if(!file.exists()){
            try {
                Config c = ConfigFactory.empty();
                for(Langs la : Langs.values()) {
                    if (la.getDefault().size() == 1) {
                        String s = la.getDefault().get(0);
                        c = c.withValue(la.convert(), ConfigValueFactory.fromAnyRef(s));
                    } else {
                        c = c.withValue(la.convert(), ConfigValueFactory.fromAnyRef(la.getDefault()));
                    }
                }
                ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
                Files.write(file.toPath(), c.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        language = ConfigFactory.parseFile(file);
    }

    public void reload(){
        if(language == null){
            create();
        }
        language = ConfigFactory.parseFile(file).resolve();
    }

    public Config getLanguage() {
        return language;
    }

}
