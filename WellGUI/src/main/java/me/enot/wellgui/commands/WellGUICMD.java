package me.enot.wellgui.commands;

import me.enot.wellgui.configurations.Settings;
import me.enot.wellgui.configurations.language.Langs;
import me.enot.wellgui.configurations.language.Language;
import me.enot.wellgui.configurations.language.Replace;
import me.enot.wellgui.gui.serializable.Serialization;
import me.enot.wellgui.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WellGUICMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("wellgui")) {
            if (s.hasPermission(Settings.getInstance().getPermissionReloadAll()) ||
                    s.hasPermission(Settings.getInstance().getPermissionReloadConfigs()) ||
                    s.hasPermission(Settings.getInstance().getPermissionReloadGuis())) {
                if (args.length >= 2) {
                    switch (args[0]) {
                        case "reload":
                            switch (args[1]) {
                                case "configs":
                                    if(s.hasPermission(Settings.getInstance().getPermissionReloadConfigs())){
                                        Settings.getInstance().reload();
                                        Language.getInstance().reload();
                                        Message.getInstance().sendMessage(s, Langs.commands__wellgui__reload_configs);
                                    } else {
                                        Message.getInstance().sendMessage(s, Langs.commands__no_permissions);
                                    }
                                    break;
                                case "guis":
                                    if(s.hasPermission(Settings.getInstance().getPermissionReloadGuis())){
                                        Serialization.guis.clear();
                                        Serialization.load();
                                        Message.getInstance().sendMessage(s, Langs.commands__wellgui__reload_guis);
                                    } else {
                                        Message.getInstance().sendMessage(s, Langs.commands__no_permissions);
                                    }
                                    break;
                                case "all":
                                    if(s.hasPermission(Settings.getInstance().getPermissionReloadAll())){
                                        Settings.getInstance().reload();
                                        Language.getInstance().reload();
                                        Serialization.guis.clear();
                                        Serialization.load();
                                        Message.getInstance().sendMessage(s, Langs.commands__wellgui__reload_all);
                                    } else {
                                        Message.getInstance().sendMessage(s, Langs.commands__no_permissions);
                                    }
                                    break;
                                default:
                                    Message.getInstance().sendMessage(s, Langs.commands__wellgui__reload_argument_not_found,
                                            new Replace("\\{ARG\\}", args[1]));
                            }
                            break;
                        default:
                            Message.getInstance().sendMessage(s, Langs.commands__wellgui__help);
                            break;
                    }
                }
            } else {
                Message.getInstance().sendMessage(s, Langs.commands__no_permissions);
            }
        }
        return false;
    }
}
