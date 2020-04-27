package me.enot.events.commands;

import me.enot.settings.Language;
import me.enot.settings.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WellOnlineCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("wellonline")){
            if(s.hasPermission(Main.getMain().getString("permission-for-wellonline-command"))){
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("reload")){
                        Main.reload();
                        Language.reload();
                        s.sendMessage(Language.get("reload_" + Main.getLang()));
                    }
                } else {
                    s.sendMessage(Language.get("reload-use_" + Main.getLang()));
                }
            } else {
                s.sendMessage(Language.get("no-perms_" + Main.getLang()));
            }
        }
        return false;
    }
}
