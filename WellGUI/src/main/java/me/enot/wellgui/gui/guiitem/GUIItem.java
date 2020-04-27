package me.enot.wellgui.gui.guiitem;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUIItem {

    private String id;
    private ItemStack stack;
    private int slot;
    private GUIItemType guiItemType;
    private String accessPermission;
    private List<String> noAccess;
    private List<String> hasAccess;

    private List<String> noAccessMessage;
    private List<String> hasAccessMessage;

    public GUIItem(String id, ItemStack stack, int slot, GUIItemType guiItemType,
                   String accessPermission, List<String> noAccess, List<String> hasAccess,
                   List<String> noAccessMessage, List<String> hasAccessMessage){
        this.id = id;
        this.stack = stack;
        this.slot = slot;
        this.guiItemType = guiItemType;
        this.accessPermission = accessPermission;
        this.noAccess = noAccess;
        this.hasAccess = hasAccess;
        this.noAccessMessage = noAccessMessage;
        this.hasAccessMessage = hasAccessMessage;
    }


    public GUIItemType getGuiItemType() {
        return guiItemType;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getStack() {
        return stack;
    }

    public String getId() {
        return id;
    }

    public List<String> getHasAccess() {
        return hasAccess;
    }

    public List<String> getNoAccess() {
        return noAccess;
    }

    public String getAccessPermission() {
        return accessPermission;
    }

    public List<String> getHasAccessMessage() {
        return hasAccessMessage;
    }

    public List<String> getNoAccessMessage() {
        return noAccessMessage;
    }

    public void setAccessPermission(String accessPermission) {
        this.accessPermission = accessPermission;
    }

    public void setGuiItemType(GUIItemType guiItemType) {
        this.guiItemType = guiItemType;
    }

    public void setHasAccess(List<String> hasAccess) {
        this.hasAccess = hasAccess;
    }

    public void setHasAccessMessage(List<String> hasAccessMessage) {
        this.hasAccessMessage = hasAccessMessage;
    }

    public void setNoAccess(List<String> noAccess) {
        this.noAccess = noAccess;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public void setNoAccessMessage(List<String> noAccessMessage) {
        this.noAccessMessage = noAccessMessage;
    }
}
