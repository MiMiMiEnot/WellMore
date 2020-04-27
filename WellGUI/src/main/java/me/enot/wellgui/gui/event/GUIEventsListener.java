package me.enot.wellgui.gui.event;

import me.enot.wellgui.WellGUI;
import me.enot.wellgui.configurations.language.Langs;
import me.enot.wellgui.configurations.language.Replace;
import me.enot.wellgui.gui.GUI;
import me.enot.wellgui.gui.guiitem.*;
import me.enot.wellgui.gui.guiitem.utils.BungeeCordUtils;
import me.enot.wellgui.gui.serializable.Serialization;
import me.enot.wellgui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class GUIEventsListener implements Listener {

    @EventHandler
    public void onClick(GUIClickEvent e){
        GUIItem item = e.getClickedItem();
        Player p = e.getPlayer();
        if (item.getAccessPermission() != null){
            if (p.hasPermission(item.getAccessPermission())){
                executeItemLogic(item, p, true);
            } else if (item.getNoAccessMessage() != null) Message.getInstance().toColoredMessage(item.getNoAccessMessage()).forEach(p::sendMessage);
        } else {
            executeItemLogic(item, p, false);
        }
    }

    private void executeItemLogic(GUIItem item, Player p, boolean iap) {
        if (item.getGuiItemType() != null) {
            if (item.getGuiItemType() instanceof GUIItemOpen) {
                GUIItemOpen gio = (GUIItemOpen) item.getGuiItemType();
                GUI gui = Serialization.getGUIByID(gio.getId());
                if (gui != null) {
                    Bukkit.getScheduler().runTask(WellGUI.getPlugin(), () -> gui.open(p));
                } else {
                    Message.getInstance().sendMessage(p, Langs.gui__click__gio__gui_not_found_by_id,
                            new Replace("\\{MESSAGE\\}", "GIO_ID = " + gio.getId() + " #NF"));
                }
            } else if (item.getGuiItemType() instanceof GUIItemSend) {
                GUIItemSend gis = (GUIItemSend) item.getGuiItemType();
                if (p.hasPermission(gis.getBypassPermission()) || BungeeCordUtils.getInstance().getServerOnline().get(gis.getServer()) < gis.getMaxOnline()) {
                    BungeeCordUtils.getInstance().sendPlayerToServer(p, gis.getServer());
                } else {
                    Message.getInstance().sendMessage(p, Langs.gui__click__gis__server_full);
                }
            } else if (item.getGuiItemType() instanceof GUIItemCommand) {
                GUIItemCommand gic = (GUIItemCommand) item.getGuiItemType();
                List<String> commands = gic.getCommands();
                commands.forEach(string -> string.replaceAll("\\{player\\}", p.getName()));
                switch (gic.getExecutor()) {
                    case PLAYER:
                        commands.forEach(p::performCommand);
                        break;
                    case CONSOLE:
                        commands.forEach(string -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string));
                        break;
                }
            }
        }
        if(item.getHasAccessMessage() != null && iap) Message.getInstance().toColoredMessage(item.getHasAccessMessage()).forEach(p::sendMessage);
    }

    /*@EventHandler
    public void onOpen(GUIOpenEvent e){
        Bukkit.broadcastMessage("GUIOpenEvent успешно вызван");
        Bukkit.broadcastMessage("Игрок " + e.getPlayer().getName() + " открыл GUI - " + e.getGui().getId());
    } */

    /*@EventHandler
    public void onClose(GUICloseEvent e){
        Bukkit.broadcastMessage("GUICloseEvent успешно вызван");
        Bukkit.broadcastMessage("Игрок " + e.getPlayer().getName() + " закрыл GUI - " + e.getGui().getId());
    }*/

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        String cmd = e.getMessage().substring(1);
        cmd = cmd.contains(" ") ? cmd.split(" ")[0] : cmd;
        GUI gui = Serialization.getGUIByCommand(cmd);
        if(gui != null){
            e.setCancelled(true);
            gui.open(e.getPlayer());
        }
    }

}