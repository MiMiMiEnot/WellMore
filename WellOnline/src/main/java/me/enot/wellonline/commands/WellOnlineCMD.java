package me.enot.wellonline.commands;

import me.enot.wellonline.configurations.Settings;
import me.enot.wellonline.configurations.language.Langs;
import me.enot.wellonline.configurations.language.Replace;
import me.enot.wellonline.data.Utils;
import me.enot.wellonline.data.mysql.MySQLHandler;
import me.enot.wellonline.events.Events;
import me.enot.wellonline.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WellOnlineCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wellonline")) {
            if(s.hasPermission(Settings.getInstance().getWellOnlinePermisison())) {
                if (args.length > 0) {
                    String server = Settings.getInstance().getServer();
                    long date = System.currentTimeMillis();
                    String playerName = args[0];
                    if (args.length == 1) {
                        Long time = MySQLHandler.getInstance().getOnlineTime(playerName, date);
                        if (time != null) {
                            String t = Utils.getTimeByLong(time);
                            Message.getInstance().sendMessage(s, Langs.commands__wellonline__history,
                                    new Replace("\\{date\\}", toNormalDate(date)),
                                    new Replace("\\{player\\}", playerName),
                                    new Replace("\\{time\\}", t));
                        } else {
                            Message.getInstance().sendMessage(s, Langs.commands__wellonline__player_history_not_found,
                                    new Replace("\\{name\\}", playerName));
                        }
                    } else if (args.length == 2) {
                        String ar2 = args[1];
                        Long time = null;
                        boolean isTime = isTimePeriod(ar2);
                        if (!isTime) {
                            server = ar2;
                            time = MySQLHandler.getInstance().getOnlineTime(playerName, date, server);
                        } else {
                            SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
                            if (ar2.contains("-")) {
                                String[] ss = ar2.split("-");
                                try {
                                    Long d1 = Utils.convertToDayStart(f.parse(ss[0]).getTime());
                                    Long d2 = Utils.convertToDayEnd(f.parse(ss[1]).getTime());
                                    time = MySQLHandler.getInstance().getOnlineTime(playerName, d1, d2, server);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    time = MySQLHandler.getInstance().getOnlineTime(playerName, f.parse(ar2).getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (time != null) {
                            Message.getInstance().sendMessage(s, Langs.commands__wellonline__history,
                                    new Replace("\\{date\\}", !isTime ? toNormalDate(date) : ar2),
                                    new Replace("\\{player\\}", playerName),
                                    new Replace("\\{time\\}", Utils.getTimeByLong(time)));
                        } else Message.getInstance().sendMessage(s, Langs.commands__wellonline__player_history_not_found,
                                new Replace("\\{name\\}", playerName));
                    } else {
                        String arg1 = args[1];
                        boolean isTime1 = isTimePeriod(args[1]);
                        boolean isTime2 = isTimePeriod(args[2]);
                        String d = null;
                        if (isTime1) {
                            d = arg1;
                            server = args[2];
                        } else if (isTime2) {
                            d = args[2];
                            server = arg1;
                        }
                        Long time = null;
                        if (d != null) {
                            SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
                            if (d.contains("-")) {
                                String[] ss = d.split("-");
                                try {
                                    Long d1 = Utils.convertToDayStart(f.parse(ss[0]).getTime());
                                    Long d2 = Utils.convertToDayEnd(f.parse(ss[1]).getTime());
                                    time = MySQLHandler.getInstance().getOnlineTime(playerName, d1, d2, server);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    time = MySQLHandler.getInstance().getOnlineTime(playerName, f.parse(d).getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (time != null) {
                                Message.getInstance().sendMessage(s, Langs.commands__wellonline__history,
                                        new Replace("\\{date\\}", d),
                                        new Replace("\\{player\\}", playerName),
                                        new Replace("\\{time\\}", Utils.getTimeByLong(time)));
                            } else Message.getInstance().sendMessage(s, Langs.commands__wellonline__player_history_not_found,
                                    new Replace("\\{name\\}", playerName));
                        } else {
                            Message.getInstance().sendMessage(s, Langs.commands__wellonline__invalid_date,
                                    new Replace("\\{date\\}", isTime1 ? arg1 : args[2]));
                        }
                    }
                    if (Events.map.containsKey(playerName.toLowerCase())) {
                        Message.getInstance().sendMessage(s, Langs.commands__wellonline__player_online,
                                new Replace("\\{player\\}", playerName),
                                new Replace("\\{time\\}", Utils.getTimeByLong(Events.map.get(playerName.toLowerCase()))));
                    }
                } else Message.getInstance().sendMessage(s, Langs.commands__wellonline__usage);
            } else {
                Message.getInstance().sendMessage(s, Langs.commands__wellonline__no_permissions);
            }
        }

        return false;
    }

    private boolean isTimePeriod(String argument) {
        if (argument.contains("-")) {
            String[] s = argument.split("-");
            if (s.length == 2) {
              return isData(s[0]) && isData(s[1]);
            }
        } else return isData(argument);
        return false;
    }

    private boolean isData(String argument) {
        String[] s = argument.split("\\.");
        if (s.length == 3) {
            if (s[0].split("").length == 2) {
                if (s[1].split("").length == 2) {
                    return s[2].split("").length == 4;
                }
            }
        }
        return false;
    }

    private String toNormalDate(Long d) {
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date(d);
        return f.format(date);
    }
}