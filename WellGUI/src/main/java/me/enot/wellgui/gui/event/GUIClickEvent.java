package me.enot.wellgui.gui.event;

import me.enot.wellgui.gui.GUI;
import me.enot.wellgui.gui.guiitem.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GUIClickEvent extends Event implements Cancellable {

    private GUI gui;
    private GUIItem clickedItem;
    private Player player;
    private int clickedSlot;

    private boolean isCancelled;

    public GUIClickEvent(GUI gui, GUIItem clickedItem, Player player, int clickedSlot){
        this.gui = gui;
        this.clickedItem = clickedItem;
        this.player = player;
        this.clickedSlot = clickedSlot;

        this.isCancelled = false;
    }

    public GUI getGui() {
        return gui;
    }

    public GUIItem getClickedItem() {
        return clickedItem;
    }

    public Player getPlayer() {
        return player;
    }

    public int getClickedSlot() {
        return clickedSlot;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
