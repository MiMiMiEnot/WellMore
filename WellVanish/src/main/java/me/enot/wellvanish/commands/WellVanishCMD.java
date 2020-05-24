package me.enot.wellvanish.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.enot.wellvanish.WellVanish;
import me.enot.wellvanish.configurations.Settings;
import me.enot.wellvanish.configurations.language.Langs;
import me.enot.wellvanish.configurations.language.Replace;
import me.enot.wellvanish.events.VanishEvents;
import me.enot.wellvanish.log.Logger;
import me.enot.wellvanish.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WellVanishCMD implements CommandExecutor {

    private static HashMap<String, Logger> list = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wellvanish")) {
            if (s.hasPermission(Settings.getInstance().getPermissionVanish())) {
                if (args.length == 0) {
                    if (s instanceof Player) {
                        Player p = (Player) s;
                        if (list.containsKey(p.getName())) {
                            Logger l = list.get(p.getName());
                            l.log();
                            Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(WellVanish.getPlugin(), p));
                            list.remove(p.getName(), l);
                            Message.getInstance().sendMessage(p, Langs.commands__wellvanish__vanish__disabled);
                            VanishEvents.vanishMap.remove(p.getName());
                            p.teleport(l.getLocation());
                        } else Message.getInstance().sendMessage(p, Langs.commands__wellvanish__vanish__usage);
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("history")) {
                        if (s.hasPermission(Settings.getInstance().getPermissionVanishHistory())) {
                            Message.getInstance().sendMessage(s, Langs.commands__wellvanish__history__usage);
                        } else {
                            Message.getInstance().sendMessage(s, Langs.commands__wellvanish__no_permissions);
                        }
                    } else {
                        Player p = Bukkit.getPlayer(args[0]);
                        if (p == null) {
                            Message.getInstance().sendMessage(s, Langs.commands__wellvanish__vanish__player_offline,
                                    new Replace("\\{name\\}", args[0]));
                        } else {
                            Player player = (Player) s;
                            Logger logger = new Logger(player.getName(), System.currentTimeMillis(), player.getLocation());
                            VanishEvents.vanishMap.add(player.getName());
                            list.put(player.getName(), logger);
                            Bukkit.getOnlinePlayers().forEach(pp -> {
                                if (!pp.hasPermission(Settings.getInstance().getPermissionVanishBypass()))
                                    pp.hidePlayer(WellVanish.getPlugin(), player);
                            });
                            player.teleport(p.getLocation());
                            Message.getInstance().sendMessage(player, Langs.commands__wellvanish__vanish__enabled,
                                    new Replace("\\{name\\}", p.getName()));
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("history")) {
                        if (s.hasPermission(Settings.getInstance().getPermissionVanishHistory())) {
                            String moderName = args[1];
                            File f = new File(WellVanish.getPlugin().getDataFolder() + "/data/", moderName + ".json");
                            if (f.exists()) {
                                try {
                                    Gson gson = new Gson();
                                    final Type TYPE = new TypeToken<List<Logger>>() {
                                    }.getType();
                                    Gson json = new Gson();
                                    JsonReader reader = new JsonReader(new FileReader(f));
                                    List<Logger> data = json.fromJson(reader, TYPE);
                                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                                    data.forEach(logger ->
                                        Message.getInstance().sendMessage(s, Langs.commands__wellvanish__history__entry,
                                                new Replace("\\{date\\}", format.format(new Date(logger.getDate()))),
                                                new Replace("\\{name\\}", moderName),
                                                new Replace("\\{time\\}", getTimeByLong(logger.getVanishTime())))
                                    );
                                } catch (FileNotFoundException e) {}
                            } else {
                                Message.getInstance().sendMessage(s, Langs.commands__wellvanish__history__not_found,
                                        new Replace("\\{name\\}", moderName));
                            }
                        } else {
                            Message.getInstance().sendMessage(s, Langs.commands__wellvanish__no_permissions);
                        }
                    } else {
                        Player p = Bukkit.getPlayer(args[0]);
                        if (p == null) {
                            Message.getInstance().sendMessage(s, Langs.commands__wellvanish__vanish__player_offline,
                                    new Replace("\\{name\\}", args[0]));
                        } else {
                            Player player = (Player) s;
                            Logger logger = new Logger(player.getName(), System.currentTimeMillis(), player.getLocation());
                            VanishEvents.vanishMap.add(player.getName());
                            list.put(player.getName(), logger);
                            Bukkit.getOnlinePlayers().forEach(pp -> {
                                if (!pp.hasPermission(Settings.getInstance().getPermissionVanishBypass()))
                                    pp.hidePlayer(WellVanish.getPlugin(), player);
                            });
                            player.teleport(p.getLocation());
                            Message.getInstance().sendMessage(player, Langs.commands__wellvanish__vanish__enabled,
                                    new Replace("\\{name\\}", p.getName()));
                        }
                    }
                }

                /*if (args.length >= 1 && args[0].equalsIgnoreCase("history")) {
                    if (s.hasPermission(Settings.getInstance().getPermissionVanishHistory())) {

                    } else Message.getInstance().sendMessage(s, Langs.commands__wellvanish__no_permissions);
                } else {
                    normal logic
                }*/
            } else Message.getInstance().sendMessage(s, Langs.commands__wellvanish__no_permissions);
        }
        return false;
    }

    private String getTimeByLong(long time) {
        StringBuilder builder = new StringBuilder();
        time /= 1000; // В секунды
        final int hour = 3600;
        final int minute = 60;

        int hours = (int) (time > hour ? time / hour : 0);
        time -= hours * hour;
        int minutes = (int) (time > minute ? time / minute : 0);
        time -= minutes * minute;
        int seconds = (int) time;

        String h = (String) Message.getInstance().getObjectMessage(Langs.time_unit__hours);
        String m = (String) Message.getInstance().getObjectMessage(Langs.time_unit__minute);
        String s = (String) Message.getInstance().getObjectMessage(Langs.time_unit__second);

        if (hours > 0) builder.append(hours).append(h);
        if (hours > 0) builder.append(minutes).append(m);
        if (hours > 0) builder.append(seconds).append(s);

        return builder.toString();
    }
}
