package me.enot.wellgui.gui.event;

import me.enot.wellgui.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GUICloseEvent extends Event {

    private GUI gui;
    private Player player;

    public GUICloseEvent(GUI gui, Player player){
        this.gui = gui;
        this.player = player;
    }

    public GUI getGui() {
        return gui;
    }

    public Player getPlayer() {
        return player;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
