package me.enot.wellchat.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import me.enot.wellchat.configurations.Settings;
import me.enot.wellchat.configurations.language.Replace;
import me.enot.wellchat.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.NumberConversions;

public class ChatFormatter implements Listener {

    private final String prefixL = "&8[&7L&8] ";
    private final String prefixG = "&f[&eG&f] ";
    private final String prefixSpy = "&6SPY ";

    private final String format = "&r%%luckperms_prefix%% {name} %%luckperms_suffix%% &8➯&f {message}";

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){
        String message = e.getMessage();
        //Bukkit.getConsoleSender().sendMessage(e.getFormat());
        String f;
        Player p = e.getPlayer();
        String prefix = PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%");
        String suffix = PlaceholderAPI.setPlaceholders(p, "%luckperms_suffix%");
        if (Settings.getInstance().isAntiMatEnabled()) message = ChatUtils.withoutBedWords(message, p);
        if(Settings.getInstance().isLocalChatEnabled()){
            if(message.startsWith("!")) {
                int substring = 1;
                if(message.startsWith("! ")) substring = 2;
                e.setMessage(message.substring(substring));
                f = ChatColor.translateAlternateColorCodes('&', prefixG + prefix + " %1$s " + suffix + " &8➯&f %2$s");
                e.getRecipients().clear();
                Bukkit.getOnlinePlayers().forEach(player -> e.getRecipients().add(player));
            } else {
                f = ChatColor.translateAlternateColorCodes('&', prefixL + prefix + " %1$s " + suffix + " &8➯&f %2$s");
                e.getRecipients().clear();
                for(Player player : p.getWorld().getPlayers()){
                    double r = NumberConversions.square(p.getLocation().getBlockX() - player.getLocation().getBlockX())
                            + NumberConversions.square(p.getLocation().getBlockZ() - player.getLocation().getBlockZ());
                    double i = Math.sqrt(r);
                    if (i <= Settings.getInstance().getLocalChatRadius()){
                        e.getRecipients().add(player);
                    }
                }
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission(Settings.getInstance().getPermissionSpy())) {
                        if (!e.getRecipients().contains(player)) player.sendMessage(
                                Message.getInstance().toColoredMessage(PlaceholderAPI.setPlaceholders(p, prefixSpy + format),
                                        new Replace("\\{name\\}", p.getName()),
                                        new Replace("\\{message\\}", message))
                        );
                    }
                }
                e.setMessage(message);
            }
        } else {
            f = ChatColor.translateAlternateColorCodes('&', prefix + " %1$s " + suffix + " &8➯&f %2$s");
            e.getRecipients().clear();
            Bukkit.getOnlinePlayers().forEach(player -> e.getRecipients().add(player));
            e.setMessage(message);
        }
        //Bukkit.getConsoleSender().sendMessage(f);
        e.setFormat(f);
        double r = NumberConversions.square(p.getLocation().getBlockX() - 1)
                + NumberConversions.square(p.getLocation().getBlockZ() - 1);
        double i = Math.sqrt(r);
        Bukkit.getConsoleSender().sendMessage(e.getPlayer().getName() + " " + i);
    }

}
