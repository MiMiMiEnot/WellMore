package me.enot.wellgui;

import me.enot.wellgui.commands.WellGUICMD;
import me.enot.wellgui.configurations.Settings;
import me.enot.wellgui.configurations.language.Language;
import me.enot.wellgui.gui.event.GUIEventsListener;
import me.enot.wellgui.gui.event.util.GUIClickEventCaller;
import me.enot.wellgui.gui.guiitem.utils.BungeeCordUtils;
import me.enot.wellgui.gui.serializable.Serialization;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class WellGUI extends JavaPlugin {

    public static Plugin getPlugin(){
        return WellGUI.getPlugin(WellGUI.class);
    }

    @Override
    public void onEnable(){
        Settings.getInstance().reload();
        Language.getInstance().reload();

        Serialization.load();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordUtils());

        Bukkit.getPluginManager().registerEvents(new GUIEventsListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIClickEventCaller(), this);
        getCommand("wellgui").setExecutor(new WellGUICMD());
    }

    @Override
    public void onDisable(){

    }
}
