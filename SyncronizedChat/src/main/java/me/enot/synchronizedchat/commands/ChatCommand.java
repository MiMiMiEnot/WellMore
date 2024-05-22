package me.enot.synchronizedchat.commands;

import com.google.gson.Gson;
import me.enot.synchronizedchat.SynchronizedChat;
import me.enot.synchronizedchat.chat.ValidationUtil;
import me.enot.synchronizedchat.chat.listener.ChatFormatter;
import me.enot.synchronizedchat.chat.obj.ChatEntry;
import me.enot.synchronizedchat.chat.obj.ChatPlayer;
import me.enot.synchronizedchat.chat.obj.util.Reject;
import me.enot.synchronizedchat.chat.obj.util.Warning;
import me.enot.synchronizedchat.configurations.Settings;
import me.enot.synchronizedchat.configurations.language.Langs;
import me.enot.synchronizedchat.configurations.language.Language;
import me.enot.synchronizedchat.configurations.language.Replace;
import me.enot.synchronizedchat.subscriber.obj.BadWordAddObject;
import me.enot.synchronizedchat.subscriber.obj.BadWordRemoveObject;
import me.enot.synchronizedchat.utils.Message;
import me.enot.synchronizedchat.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatCommand implements TabExecutor {
    private final Gson gson = new Gson();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cmd.getName().equals("chat")) return true;
        if (cs instanceof Player && !PermissionUtil.Perm.COMMANDS__CHAT__USAGE.has((Player) cs)) {
            Message.getInstance().sendMessage(cs, Langs.commands__without_permission);
            return true;
        }

        if (args.length < 1) {
            Message.getInstance().sendMessage(cs, Langs.commands__chat__usage);
            return true;
        }
        boolean console = !(cs instanceof Player);

        String subcommands = args[0].toLowerCase();
        switch (subcommands) {
            case "reload":
                if (console || PermissionUtil.Perm.COMMANDS__CHAT__RELOAD.has((Player) cs)) {
                    Settings.getInstance().reload();
                    Language.getInstance().reload();
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__reload__reloaded);
                } else Message.getInstance().sendMessage(cs, Langs.commands__without_permission);
                break;
            case "test":
                if (console || PermissionUtil.Perm.COMMANDS__CHAT__TEST.has((Player) cs)) {
                    if (args.length < 2) {
                        Message.getInstance().sendMessage(cs, Langs.commands__chat__test__usage);
                        return true;
                    }
                    // Создаём стринг сообщения
                    // TODO: 07.08.2021 сделать на основе масива получше, переделать чтобы не через билдер
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]);
                    }
                    String message = builder.toString();
                    ChatEntry ce = new ChatEntry(message);
                    ValidationUtil.fullValidateTest(ce);
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__test__testing,
                            new Replace("{original}", ce.getMessage()),
                            new Replace("{rejects}", Arrays.stream(ce.getRejects()).map(Reject::name).collect(Collectors.joining(", "))),
                            new Replace("{warnings}", Arrays.stream(ce.getWarnings()).map(Warning::name).collect(Collectors.joining(", "))),
                            new Replace("{edited}", ce.getEditedMessage()));

                } else Message.getInstance().sendMessage(cs, Langs.commands__without_permission);
                break;
            case "add":
                if (!console && !PermissionUtil.Perm.COMMANDS__CHAT__ADD.has((Player) cs)) {
                    Message.getInstance().sendMessage(cs, Langs.commands__without_permission);
                    return true;
                }
                if (args.length < 2) {
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__add__usage);
                    return true;
                }
                String badword = args[1].toLowerCase();
                if (Settings.getInstance().getBadWords().contains(badword)) {
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__add__already,
                            new Replace("{word}", badword));
                    return true;
                }
                Settings.getInstance().getBadWords().add(badword);
                Settings.getInstance().saveBadWords();
                Settings.getInstance().reload();
                SynchronizedChat.getRedisCore().message(Settings.getInstance().getChannelName(), gson.toJson(new BadWordAddObject(badword)));
                Message.getInstance().sendMessage(cs, Langs.commands__chat__add__successful,
                        new Replace("{word}", badword));
                break;
            case "remove":
                if (!console && !PermissionUtil.Perm.COMMANDS__CHAT__REMOVE.has((Player) cs)) {
                    Message.getInstance().sendMessage(cs, Langs.commands__without_permission);
                    return true;
                }
                if (args.length < 2) {
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__remove__usage);
                    return true;
                }
                String bw = args[1].toLowerCase();
                if (!Settings.getInstance().getBadWords().contains(bw)) {
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__remove__already,
                            new Replace("{word}", bw));
                    return true;
                }
                Settings.getInstance().getBadWords().remove(bw);
                Settings.getInstance().saveBadWords();
                Settings.getInstance().reload();
                SynchronizedChat.getRedisCore().message(Settings.getInstance().getChannelName(), gson.toJson(new BadWordRemoveObject(bw)));
                Message.getInstance().sendMessage(cs, Langs.commands__chat__remove__successful,
                        new Replace("{word}", bw));
                break;
            case "history":
                if (!console && !PermissionUtil.Perm.COMMANDS__CHAT__HISTORY.has((Player) cs)) {
                    Message.getInstance().sendMessage(cs, Langs.commands__without_permission);
                    return true;
                }

                if (args.length < 2) {
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__history__usage);
                    return true;
                }
                String name = args[1];
                int max = -1;
                if (args.length > 2) {
                    try {
                        max = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                ChatPlayer chatPlayer = ChatFormatter.players.get(name);
                if (chatPlayer == null || chatPlayer.getChatHistory().size() < 1) {
                    Message.getInstance().sendMessage(cs, Langs.commands__chat__history__empty, new Replace("{username}", name));
                    return true;
                }
                int chatHistorySize = chatPlayer.getChatHistory().size();
                Message.getInstance().sendMessage(cs, Langs.commands__chat__history__title, new Replace("{X}", max), new Replace("{username}", name));
                for (int i = 0; i < chatHistorySize; i++) {
                    if (max != -1 && ((i + 1) > max)) {
                        break;
                    }
                    ChatEntry chatEntry = chatPlayer.getChatHistory().get(chatHistorySize-i-1);
                    if (chatEntry == null) {
                        Bukkit.getConsoleSender().sendMessage("entry is null, " + i + "(from " + chatHistorySize + ")");
                        continue;
                    }
                    Message.getInstance().sendMessage(cs,
                            Langs.commands__chat__history__entry,
                            new Replace("{time}", simpleDateFormat.format(new Date(chatEntry.getTime()))),
                            new Replace("{original}", chatEntry.getMessage()));

                }
                break;
            default:
                Message.getInstance().sendMessage(cs, Langs.commands__chat__usage);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 2)
            return Arrays.asList("reload", "test", "add", "remove", "history");
        return null;
    }
}
