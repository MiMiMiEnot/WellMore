package me.enot.wellgui.gui;

import me.enot.wellgui.configurations.language.Replace;
import me.enot.wellgui.gui.guiitem.GUIItem;
import me.enot.wellgui.gui.guiitem.utils.GUIItemUtils;
import me.enot.wellgui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.enot.wellgui.configurations.language.Langs.gui__open__please_wait;

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
//        GUIItem item = null;
//        for (int i = 0; i < this.items.size(); i++) {
//            if ()
//        }
//        return item;
        for(GUIItem item : this.items){
            //Bukkit.getConsoleSender().sendMessage(" " + slot + " == " + item.getSlot());
            if (item.getSlot() == slot) return item;
        }
        return null;
    }

    public GUIItem getGUIItemBySlotAndPlayer(int slot, Player player) {
        GUIItem item = null;

        for (int i = 0; i < this.items.size(); i++) {
            GUIItem g = this.items.get(i);
            if (g.getSlot() == slot && g.getVisible() != null && player.hasPermission(g.getVisible())) {
                item = g;
            } else if (g.getSlot() == slot && g.getVisible() == null) item = getGUIItemBySlot(slot);
        }

        return item;
    }

    public void open(Player player){
        try {
            player.openInventory(constructInventory(player));
        } catch (IllegalArgumentException e) {}
    }

    private Inventory constructInventory(Player player){
        Inventory inventory = Bukkit.createInventory(null, this.rows * 9, this.title);
        this.items.forEach(item -> {
            if (item.getVisible() != null) {
                if (player.hasPermission(item.getVisible())) {
                    try {
                        inventory.setItem(item.getSlot(), GUIItemUtils.replaceAllInItem(item.getStack(), player, item));
                    } catch (NullPointerException e) {
                        Message.getInstance().sendMessage(player, gui__open__please_wait);
                        throw new IllegalArgumentException();
                    }
                }
            } else {
                try {
                    inventory.setItem(item.getSlot(), GUIItemUtils.replaceAllInItem(item.getStack(), player, item));
                } catch (NullPointerException e) {
                    Message.getInstance().sendMessage(player, gui__open__please_wait);
                    throw new IllegalArgumentException();
                }
            }
        });
        return inventory;
    }

}
