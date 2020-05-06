package me.enot.wellgui.gui;

import me.enot.wellgui.configurations.language.Replace;
import me.enot.wellgui.gui.guiitem.GUIItem;
import me.enot.wellgui.gui.guiitem.utils.GUIItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUI {

    private String id;

    private List<GUIItem> items;
    private String title;
    private String command;
    private int rows;

    public GUI(String id, List<GUIItem> items, String title, String command, int rows){
        this.id = id;
        this.items = items;
        this.title = title;
        this.command = command;
        this.rows = rows;
    }

    public String getId() {
        return id;
    }

    public List<GUIItem> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public String getCommand() {
        return command;
    }

    public int getRows() {
        return rows;
    }

    public GUIItem getGUIItemByID(String id){
        for(GUIItem item : this.items){
            if(item.getId().equalsIgnoreCase(id)) return item;
        }
        return null;
    }

    public GUIItem getGUIItemBySlot(int slot){
        for(GUIItem item : this.items){
            //Bukkit.getConsoleSender().sendMessage(" " + slot + " == " + item.getSlot());
            if(item.getSlot() == slot) return item;
        }
        return null;
    }

    public void open(Player player){
        player.openInventory(constructInventory(player));
    }

    private Inventory constructInventory(Player player){
        Inventory inventory = Bukkit.createInventory(null, this.rows * 9, this.title);
        this.items.forEach(item ->
            inventory.setItem(item.getSlot(), GUIItemUtils.replaceAllInItem(item.getStack(), player, item)));
        return inventory;
    }

}
