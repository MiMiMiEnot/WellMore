package me.enot.synchronizedchat.commands;

import me.enot.synchronizedchat.SynchronizedChat;
import me.enot.synchronizedchat.chat.listener.ChatFormatter;
import me.enot.synchronizedchat.chat.obj.ChatPlayer;
import me.enot.synchronizedchat.configurations.language.Langs;
import me.enot.synchronizedchat.configurations.language.Replace;
import me.enot.synchronizedchat.data.DataBaseUtil;
import me.enot.synchronizedchat.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IgnoreCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ignore")) {
            if (!(cs instanceof Player)) return true;
            Player player = (Player) cs;
            if (args.length < 1) {
                Message.getInstance().sendMessage(player, Langs.commands__ignore__usage);
                return true;
            }
            String username = args[0];
            if (username.equalsIgnoreCase(player.getName())) {
                Message.getInstance().sendMessage(player, Langs.commands__ignore__yourself);
                return true;
            }
            ChatPlayer chatPlayer = ChatFormatter.players.get(player.getName());
            if (chatPlayer == null) {
                chatPlayer = new ChatPlayer(player.getName());
                ChatFormatter.players.put(player.getName(), chatPlayer);
            }
            if (chatPlayer.getIgnored().stream().noneMatch(s -> s.equalsIgnoreCase(username))) {
                chatPlayer.getIgnored().add(username);
//                chatPlayer.getIgnored().forEach(Bukkit::broadcastMessage);
                DataBaseUtil.ignoreChange(SynchronizedChat.getMySQL(), chatPlayer);
                Message.getInstance().sendMessage(player, Langs.commands__ignore__add, new Replace("{username}", username));
            } else {
                chatPlayer.getIgnored().remove(username);
//                chatPlayer.getIgnored().forEach(Bukkit::broadcastMessage);
                DataBaseUtil.ignoreChange(SynchronizedChat.getMySQL(), chatPlayer);
                Message.getInstance().sendMessage(player, Langs.commands__ignore__remove, new Replace("{username}", username));
            }
        }

        return true;
    }

//    @Override
//    public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
////        if (cs instanceof Player && args.length < 2) {
////            // Берём игрока с списка пользователей чата
////            ChatPlayer player = ChatFormatter.players.get(cs.getName());
////            // Если игрок найден возвращаем его список игнора, если его не нашли возвращаем ничего
////            return player == null ? null : new ArrayList<>(player.getIgnored());
////        }
//        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
//    }
}
