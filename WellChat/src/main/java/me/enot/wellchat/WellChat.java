package me.enot.wellchat;

import me.enot.wellchat.chat.ChatFormatter;
import me.enot.wellchat.commands.WellChatCMD;
import me.enot.wellchat.configurations.Settings;
import me.enot.wellchat.configurations.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WellChat extends JavaPlugin {

    public static Plugin getPlugin(){
        return WellChat.getPlugin(WellChat.class);
    }

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getConsoleSender().sendMessage("PlaceholderAPI не найден");
            this.setEnabled(false);
        }
        Settings.getInstance().reload();
        Language.getInstance().reload();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ChatFormatter(), this);
        getCommand("wellchat").setExecutor(new WellChatCMD());
    }

    @Override
    public void onDisable() {

    }
}
