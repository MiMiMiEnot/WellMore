package me.enot.events.commands;

import me.enot.WellOnline;
import me.enot.events.utils.User;
import me.enot.settings.Language;
import me.enot.settings.Main;
import me.enot.utils.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Check implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("check")) {
            if (s.hasPermission(Main.getMain().getString("permission-for-check-command"))) {
                if(WellOnline.mysql) {
                    if (args.length >= 2) {
                        int count = Main.getMain().getInt("standart-history-height");
                        if (args.length > 2) {
                            if (toInt(args[2])) {
                                count = Integer.parseInt(args[2]);
                            } else {
                                s.sendMessage(Language.get("not-a-number_" + Main.getLang()).
                                        replace("{X}", args[2]).
                                        replace("{Y}", Integer.toString(count)));
                            }
                        }
                        ArrayList<User> list = MySQL.getUserListByNum(count, args[0].toLowerCase(), args[1]);
                        if(list != null) {
                            s.sendMessage(Language.get("last-enters_" + Main.getLang()).
                                    replace("{X}", Integer.toString(count)).
                                    replace("{NICK}", args[0]).
                                    replace("{SERVER}", args[1]));
                            for (User u : list) {
                                String pattern = Main.getMain().getString("date-format");
                                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                                //s.sendMessage("-> " + sdf.format(new Date(u.getDate())) + " " + toNormalTime(u.getTime()));
                                s.sendMessage(Language.get("line_" + Main.getLang())
                                        .replace("{DATE}", sdf.format(new Date(u.getDate())))
                                        .replace("{TIME}", toNormalTime(u.getTime())));
                            }
                        } else {
                            s.sendMessage(Language.get("history-not-found_" + Main.getLang()));
                        }
                    } else {
                        s.sendMessage(Language.get("check-use_" + Main.getLang()));
                    }
                } else {
                    if(args.length >= 1){
                        int count = Main.getMain().getInt("standart-history-height");
                        if(args.length > 1){
                            if(toInt(args[1])){
                                count = Integer.parseInt(args[1]);
                            } else {
                                s.sendMessage(Language.get("not-a-number_" + Main.getLang()).
                                        replace("{X}", args[2]).
                                        replace("{Y}", Integer.toString(count)));
                            }
                        }
                    } else {
                        s.sendMessage(Language.get("check-use-without-mysql_" + Main.getLang()));
                    }
                }
            } else {
                s.sendMessage(Language.get("no-perms_" + Main.getLang()));
            }
        }
        return false;
    }

    private static boolean toInt(String s){
        try {
            int i = Integer.parseInt(s);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private static String toNormalTime(Long millis){
        return String.format(Language.get("time-format_" + Main.getLang()), TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
