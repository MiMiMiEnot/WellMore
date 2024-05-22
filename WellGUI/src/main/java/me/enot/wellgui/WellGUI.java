package me.enot.wellgui;

import me.enot.wellgui.commands.WellGUICMD;
import me.enot.wellgui.commands.WellSendCommand;
import me.enot.wellgui.configurations.Settings;
import me.enot.wellgui.configurations.language.Language;
import me.enot.wellgui.gui.event.GUIEventsListener;
import me.enot.wellgui.gui.event.util.GUIClickEventCaller;
import me.enot.wellgui.gui.guiitem.utils.BungeeCordUtils;
import me.enot.wellgui.gui.guiitem.utils.ServerPinger;
import me.enot.wellgui.gui.guiitem.utils.ServerPingerEvents;
import me.enot.wellgui.gui.serializable.Serialization;
import me.enot.wellgui.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;


public class WellGUI extends JavaPlugin {

    public static Plugin getPlugin(){
        return WellGUI.getPlugin(WellGUI.class);
    }

    private static ServerPinger pinger;
    private static MySQL mysql = null;

    @Override
    public void onEnable(){
        Settings.getInstance().reload();
        Language.getInstance().reload();

        Serialization.load();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordUtils());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "legacy:redisbungee");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "legacy:redisbungee", new BungeeCordUtils());

        Bukkit.getPluginManager().registerEvents(new GUIEventsListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIClickEventCaller(), this);
        Bukkit.getPluginManager().registerEvents(new ServerPingerEvents(), this);
        getCommand("wellgui").setExecutor(new WellGUICMD());
        getCommand("guisend").setExecutor(new WellSendCommand());
        pinger = new ServerPinger();
//        pinger.start();
        mysql = new MySQL();
    }

    public static ServerPinger getPinger() {
        return pinger;
    }

    public static MySQL getMysql() {
        return mysql;
    }

    @Override
    public void onDisable(){
        if (mysql != null) {
            try {
                mysql.getConnection().close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
