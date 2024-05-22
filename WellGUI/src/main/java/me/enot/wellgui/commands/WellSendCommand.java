package me.enot.wellgui.commands;

import me.enot.wellgui.WellGUI;
import me.enot.wellgui.configurations.language.Langs;
import me.enot.wellgui.configurations.language.Replace;
import me.enot.wellgui.gui.guiitem.utils.BungeeCordUtils;
import me.enot.wellgui.sql.DataBaseUtil;
import me.enot.wellgui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;

public class WellSendCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("guisend")) {
            if (s instanceof ConsoleCommandSender || ((Player) s).hasPermission("wellgui.command.guisend")) {
                if (args.length >= 2) {
                    String name = args[0];
                    String to = args[1];
                    boolean vk = args.length > 2 && args[2].equals("+");
                    boolean isLinked = !vk;
                    if (vk) {
                        Connection connection = WellGUI.getMysql().getConnection();
                        if (connection != null) {
                            isLinked = DataBaseUtil.isLinked(connection, name);
                        } else {
                            Message.getInstance().sendMessage(s, Langs.gui__click__gis__require_vk_error,
                                    new Replace("{code}", "#GIS_" + name + "_gui_send_command"));
                        }
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    if (isLinked)
                        BungeeCordUtils.getInstance().sendPlayerToServer(Bukkit.getPlayer(name), to);
                    else
                        Message.getInstance().sendMessage(Bukkit.getPlayer(name), Langs.gui__click__gis__require_vk_link);
                    Message.getInstance().sendMessage(s, Langs.commands__guisend__send,
                            new Replace("\\{name\\}", name),
                            new Replace("\\{server\\}", to),
                            new Replace("\\{link\\}", !vk ? "не требуется" : (isLinked ? "есть" : "нет")));
                }
            } else {
                Message.getInstance().sendMessage(s, Langs.commands__no_permissions);
            }
        }

        return true;
    }
}
