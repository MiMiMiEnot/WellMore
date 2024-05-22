package me.enot.synchronizedchat.chat.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import me.enot.synchronizedchat.SynchronizedChat;
import me.enot.synchronizedchat.chat.ValidationUtil;
import me.enot.synchronizedchat.chat.obj.ChatEntry;
import me.enot.synchronizedchat.chat.obj.ChatPlayer;
import me.enot.synchronizedchat.chat.obj.util.Reject;
import me.enot.synchronizedchat.chat.obj.util.Warning;
import me.enot.synchronizedchat.configurations.Settings;
import me.enot.synchronizedchat.configurations.language.Langs;
import me.enot.synchronizedchat.configurations.language.Replace;
import me.enot.synchronizedchat.data.DataBaseUtil;
import me.enot.synchronizedchat.utils.Message;
import me.enot.synchronizedchat.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.NumberConversions;

import java.util.*;
import java.util.stream.Collectors;

public class ChatFormatter implements Listener {
    public static HashMap<String, ChatPlayer> players = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        ChatPlayer chatPlayer = players.get(player.getName());
        ChatEntry chatEntry = new ChatEntry(e.getMessage());
        // Чат плеера ещё нет, создаём нового
        if (chatPlayer == null) {
            chatPlayer = new ChatPlayer(player.getName());
            players.put(player.getName(), new ChatPlayer(player.getName()));
        }
        // Если игрок имеет права не проверяем его
        if (!PermissionUtil.Perm.CHAT_BYPASS.has(player)) {
            ValidationUtil.fullValidate(chatPlayer, chatEntry);
        }
        // Если есть причины к Reject сообщение не будет отправлено, значит просматриваем их
        Reject[] rejects = chatEntry.getRejects();
        if (rejects.length > 0 && rejects[0] != Reject.NONE) {
//        if (rejects.length == 1 && rejects[0] == Reject.NONE) {
            Message.getInstance().broadcastPermissionMessage(Langs.mod__reject, PermissionUtil.Perm.CHAT_BROADCAST.getCurrentValue(),
                    new Replace("{reject}", Message.getInstance().getMessageString(Langs.mod__reject_placeholder)),
                    new Replace("{username}", player.getName()),
                    new Replace("{message}", chatEntry.getMessage()));
            e.setCancelled(true);
        }
        // Просматриваем наличие предупреждений. Отклоняем просмотр если сообщение уже не будет отправлено по причине Rejects
        if (!e.isCancelled()) {
            Warning[] warnings = chatEntry.getWarnings();
            if (warnings.length > 0 && warnings[0] != Warning.NONE) {
                Message.getInstance().broadcastPermissionMessage(Langs.mod__warning, PermissionUtil.Perm.CHAT_BROADCAST.getCurrentValue(),
                        new Replace("{warning}", Message.getInstance().getMessageString(Langs.mod__warning_placeholder)),
                        new Replace("{username}", player.getName()),
                        new Replace("{message}", chatEntry.getMessage()));
                e.setMessage(chatEntry.getEditedMessage() != null ? chatEntry.getEditedMessage() : chatEntry.getMessage());
            }
//            Bukkit.broadcastMessage(Arrays.stream(rejects).map(Reject::name).collect(Collectors.joining(", ")));
//            Bukkit.broadcastMessage(Arrays.stream(warnings).map(Warning::name).collect(Collectors.joining(", ")));
        }
        /*if (!chatPlayer.getChatHistory().contains(chatEntry)) */chatPlayer.addChatEntry(chatEntry);
        chatPlayer.cleanUpChatHistory();
        // Если сообщение всё-же будет отправлено начинаем его форматировку
        if (e.isCancelled())
            return;

        byte chatSetting = -1; // -1 - локал чат выключен, 0 - сообщение в локал чат, 1 - сообщение в глобальный чат
        if (Settings.getInstance().isLocalChatEnabled()) {
            if (chatEntry.getMessage().startsWith("!") && !(chatEntry.getMessage().equals("!") || chatEntry.getMessage().equals("! "))) {
                chatSetting = 1;
            } else chatSetting = 0;
        }
        String prefix = PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%");
        String suffix = PlaceholderAPI.setPlaceholders(player, "%luckperms_suffix%");
        boolean suffixExist = !suffix.equals("");
        e.getRecipients().clear();
        switch (chatSetting) {
            case -1:
                ChatPlayer finalChatPlayer = chatPlayer;
                e.getRecipients().addAll(toReceiveList(finalChatPlayer));
                break;
            case 0:
                prefix = Message.getInstance().getMessageString(Langs.chat__prefix__local) + prefix;
                // Добавляем в список тех, кто получит сообщение всех тех, кто не в игноре и в радиусе конфига
                int radius = Settings.getInstance().getLocalChatRadius();
                ChatPlayer finalChatPlayer1 = chatPlayer;
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (canReceive(p, finalChatPlayer1)) {
                        double r = NumberConversions.square(p.getLocation().getBlockX() - player.getLocation().getBlockX())
                                + NumberConversions.square(p.getLocation().getBlockZ() - player.getLocation().getBlockZ());
                        double i = Math.sqrt(r);
                        if (i <= radius) e.getRecipients().add(p);
                    }
                });
                break;
            case 1:
                if (e.getMessage().startsWith("! ")) {
                    e.setMessage(e.getMessage().substring(2));
                } else if (e.getMessage().startsWith("!"))
                    e.setMessage(e.getMessage().substring(1));
                prefix = Message.getInstance().getMessageString(Langs.chat__prefix__global) + prefix;
                ChatPlayer finalChatPlayer2 = chatPlayer;
                e.getRecipients().addAll(toReceiveList(finalChatPlayer2));
                break;
        }
        e.getRecipients().add(player);
        // Устанавливаем формат сообщения
        String format = Message.getInstance().getMessageString(suffixExist ? Langs.chat__format__with_suffix : Langs.chat__format__without_suffix,
                new Replace("{prefix}", prefix),
                new Replace("{suffix}", suffix),
                new Replace("{username}", player.getName()));
        e.setFormat(format);

    }

    public static List<String> linked = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        if (Settings.getInstance().isLinkRequire() && DataBaseUtil.isLinked(SynchronizedChat.getMySQL(), e.getPlayer().getName())) {
            linked.add(e.getPlayer().getName());
        }
        String playerName = e.getPlayer().getName();
        if (!getPlayers().containsKey(playerName)) {
            getPlayers().put(playerName, new ChatPlayer(playerName));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String playerName = e.getPlayer().getName();
        if (!players.containsKey(playerName)) {
            players.put(playerName, new ChatPlayer(playerName));
        }
        e.setJoinMessage(null);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        linked.remove(e.getPlayer().getName());
        players.remove(e.getPlayer().getName());
    }

    public List<Player> toReceiveList(ChatPlayer player) {
        return Bukkit.getOnlinePlayers().stream().filter(p -> {
           ChatPlayer cp = players.get(p.getName());
           return !cp.isIgnored(player.getPlayerName());
        }).collect(Collectors.toList());
    }

    public static boolean canReceive(Player p, ChatPlayer cp) {
        if (players.get(p.getName()) == null) return false;
        return !players.get(p.getName()).isIgnored(cp.getPlayerName());
    }
    public static boolean canReceive(String p, ChatPlayer cp) {
        if (players.get(p) == null) return false;
        return !players.get(p).isIgnored(cp.getPlayerName());
    }

    public HashMap<String, ChatPlayer> getPlayers() {
        return players;
    }
}
