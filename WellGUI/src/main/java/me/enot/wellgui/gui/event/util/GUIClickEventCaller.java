package me.enot.wellgui.gui.event.util;

import me.enot.wellgui.gui.GUI;
import me.enot.wellgui.gui.event.GUIClickEvent;
import me.enot.wellgui.gui.event.GUICloseEvent;
import me.enot.wellgui.gui.event.GUIOpenEvent;
import me.enot.wellgui.gui.serializable.Serialization;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class GUIClickEventCaller implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getWhoClicked() instanceof Player) {
            GUI gui = getGUIByInventory(e.getInventory());
            if (gui != null) {
                GUIClickEvent guiClickEvent = new GUIClickEvent(gui, gui.getGUIItemBySlot(e.getSlot()), (Player) e.getWhoClicked());
                Bukkit.getPluginManager().callEvent(guiClickEvent);
                e.setCancelled(true);
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
            if(gui.getTitle().equalsIgnoreCase(inventory.getTitle())) return gui;
        }
        return null;
    }

}