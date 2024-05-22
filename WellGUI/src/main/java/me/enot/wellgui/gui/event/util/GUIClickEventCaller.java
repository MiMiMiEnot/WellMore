package me.enot.wellgui.gui.event.util;

import me.enot.wellgui.configurations.language.Langs;
import me.enot.wellgui.gui.GUI;
import me.enot.wellgui.gui.event.GUIClickEvent;
import me.enot.wellgui.gui.event.GUICloseEvent;
import me.enot.wellgui.gui.event.GUIOpenEvent;
import me.enot.wellgui.gui.serializable.Serialization;
import me.enot.wellgui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GUIClickEventCaller implements Listener {

//    private Map<String, Long> clickDelay = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getWhoClicked() instanceof Player) {
//        Bukkit.getConsoleSender().sendMessage(e.getWhoClicked().getName() + " clicked at " + getGUIByInventory(e.getInventory()).getId());
            GUI gui = getGUIByInventory(e.getInventory());
            if (gui != null)  {
//                if ((clickDelay.get(e.getWhoClicked().getName()) != null) && clickDelay.get((e.getWhoClicked()).getName()) <= System.currentTimeMillis()) {
                    GUIClickEvent guiClickEvent = new GUIClickEvent(gui,
                            gui.getGUIItemBySlotAndPlayer(e.getSlot(), (Player)e.getWhoClicked()),
                            (Player) e.getWhoClicked(),
                            e.getSlot()
                    );
//                    if (!e.getWhoClicked().hasPermission("wellgui.adm"))
//                        clickDelay.put(e.getWhoClicked().getName(), System.currentTimeMillis() + 1000);

                    Bukkit.getPluginManager().callEvent(guiClickEvent);
                    e.setCancelled(true);
//                } else {
//                    if (!e.getWhoClicked().hasPermission("wellgui.adm")) {
//                        if (clickDelay.get(e.getWhoClicked().getName()) != null) {
//                            Message.getInstance().sendMessage((Player) e.getWhoClicked(), Langs.gui__clock__to_quick);
//                            clickDelay.put(e.getWhoClicked().getName(), System.currentTimeMillis() + 1000);
//                            e.setCancelled(true);
//                        } else {
//                            clickDelay.put(e.getWhoClicked().getName(), System.currentTimeMillis() + 1000);
//                            e.setCancelled(true);
//                        }
//                    }
//                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(e.getPlayer() instanceof Player) {
            GUI gui = getGUIByInventory(e.getInventory());
            if (gui != null) {
                GUICloseEvent guiCloseEvent = new GUICloseEvent(gui, (Player) e.getPlayer());
                Bukkit.getPluginManager().callEvent(guiCloseEvent);
            }
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e){
        if(e.getPlayer() instanceof Player) {
            GUI gui = getGUIByInventory(e.getInventory());
            if (gui != null) {
                GUIOpenEvent guiOpenEvent = new GUIOpenEvent(gui, (Player) e.getPlayer());
                Bukkit.getPluginManager().callEvent(guiOpenEvent);
            }
        }
    }

    public static GUI getGUIByInventory(Inventory inventory){
        for(GUI gui : Serialization.guis){
            if(gui.getTitle().equalsIgnoreCase(inventory.getTitle()))
                return gui;
        }
        return null;
    }
}