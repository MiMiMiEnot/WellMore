package me.enot.wellchat.commands;

import me.enot.wellchat.configurations.Settings;
import me.enot.wellchat.configurations.language.Langs;
import me.enot.wellchat.configurations.language.Language;
import me.enot.wellchat.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WellChatCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("wellauth")){
            if(s.hasPermission(Settings.getInstance().getPermissionWellChatReload())) {
                if (args.length >= 1) {
                    if(args[0].equalsIgnoreCase("reload")){
                        Settings.getInstance().reload();
                        Language.getInstance().reload();
                        Message.getInstance().sendMessage(s, Langs.command__wellauth__reload_succesful);
                    } else {
                        Message.getInstance().sendMessage(s, Langs.command__wellauth__usage);
                    }
                } else {
                    Message.getInstance().sendMessage(s, Langs.command__wellauth__usage);
                }
            } else {
                Message.getInstance().sendMessage(s, Langs.command__wellauth__errors__no_perms);
            }
            return true;
        }
        return false;
    }
}
