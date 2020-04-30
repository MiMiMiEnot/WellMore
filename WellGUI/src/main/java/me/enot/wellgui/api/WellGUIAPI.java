package me.enot.wellgui.api;

import me.enot.wellgui.api.exceptions.GUINotFoundException;
import me.enot.wellgui.gui.GUI;
import me.enot.wellgui.gui.serializable.Serialization;
import org.bukkit.entity.Player;

public class WellGUIAPI {
    private WellGUIAPI() {}

    public static void openMenu(Player player, String guiId) throws GUINotFoundException {
        GUI gui = Serialization.getGUIByID(guiId);
        if (gui == null) throw new GUINotFoundException("GUI с айди " + guiId + " не найдено, " +
                "будьте уверены, что данная GUI прогружена плагином WellGUI");
        gui.open(player);
    }
}
