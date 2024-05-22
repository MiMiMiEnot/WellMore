package me.enot.synchronizedchat.commands;

import me.enot.synchronizedchat.chat.ValidationUtil;
import me.enot.synchronizedchat.chat.listener.ChatFormatter;
import me.enot.synchronizedchat.chat.obj.ChatEntry;
import me.enot.synchronizedchat.chat.obj.ChatPlayer;
import me.enot.synchronizedchat.chat.obj.util.Reject;
import me.enot.synchronizedchat.chat.obj.util.Warning;
import me.enot.synchronizedchat.configurations.language.Langs;
import me.enot.synchronizedchat.configurations.language.Replace;
import me.enot.synchronizedchat.utils.Message;
import me.enot.synchronizedchat.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PrivateMessageCommand implements CommandExecutor {

    public static HashMap<String, String> last = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("privatemessage")) {
            return true;
        }
        if (!(cs instanceof Player)) {
            return true;
        }
        Player player = (Player) cs;
        if (args.length < 2) {
            Message.getInstance().sendMessage(player, Langs.commands__private_message__usage, new Replace("{command}", label));
            return true;
        }
        String username = args[0];
        if (username.equalsIgnoreCase(player.getName())) {
            Message.getInstance().sendMessage(player, Langs.commands__private_message__yourself);
            return true;
        }
        ChatPlayer chatPlayer = ChatFormatter.players.get(player.getName());
        if (chatPlayer == null) {
            chatPlayer = new ChatPlayer(player.getName());
            ChatFormatter.players.put(player.getName(), chatPlayer);
        }
        if (!ChatFormatter.canReceive(username, chatPlayer)) {
            Message.getInstance().sendMessage(player, Langs.commands__private_message__ignored);
//            Bukkit.getConsoleSender().sendMessage("Player " + username + " is ignored by " + chatPlayer.getPlayerName() + ": " + String.join(" ", chatPlayer.getIgnored()));
            return true;
        }
//        Bukkit.getConsoleSender().sendMessage("Player " + username + " is not ignored by " + chatPlayer.getPlayerName() + ": " + String.join(" ", chatPlayer.getIgnored()));
        Player receiver = Bukkit.getPlayer(username);
        if (receiver == null) {
            Message.getInstance().sendMessage(player, Langs.commands__private_message__player_not_found, new Replace("{username}", username));
            return true;
        }
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        ChatEntry chatEntry = new ChatEntry(message);
        ValidationUtil.fullValidateTest(chatEntry);

        Reject[] rejects = chatEntry.getRejects();
        if (rejects.length > 0 && rejects[0] != Reject.NONE) {
            Message.getInstance().broadcastPermissionMessage(Langs.mod__reject_private_message, PermissionUtil.Perm.CHAT_BROADCAST.getCurrentValue(),
                    new Replace("{reject}", Message.getInstance().getMessageString(Langs.mod__reject_placeholder)),
                    new Replace("{sender}", player.getName()),
                    new Replace("{receiver}", receiver.getName()),
                    new Replace("{message}", chatEntry.getMessage()));
            return true;
        }
        Warning[] warnings = chatEntry.getWarnings();
        if (warnings.length > 0 && warnings[0] != Warning.NONE) {
            message = chatEntry.getEditedMessage();
            Message.getInstance().broadcastPermissionMessage(Langs.mod__warnings_private_message, PermissionUtil.Perm.CHAT_BROADCAST.getCurrentValue(),
                    new Replace("{warning}", Message.getInstance().getMessageString(Langs.mod__warning_placeholder)),
                    new Replace("{sender}", player.getName()),
                    new Replace("{receiver}", receiver.getName()),
                    new Replace("{message}", chatEntry.getMessage()));
        }
        last.put(player.getName(), receiver.getName());
        last.put(receiver.getName(), player.getName());
        Message.getInstance().sendMessage(player, Langs.commands__private_message__send,
                new Replace("{username}", receiver.getName()),
                new Replace("{message}", message));
        Message.getInstance().sendMessage(receiver, Langs.commands__private_message__receive,
                new Replace("{username}", player.getName()),
                new Replace("{message}", message));
        return true;
    }

}
