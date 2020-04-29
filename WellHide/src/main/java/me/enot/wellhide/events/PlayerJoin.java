package me.enot.wellhide.events;

import me.enot.wellhide.WellHide;
import me.enot.wellhide.events.util.PlayerAccount;
import me.enot.wellhide.sql.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.Set;

public class PlayerJoin implements Listener {

    private static Set<PlayerAccount> activeAccounts = new HashSet<>();

    public static Set<PlayerAccount> getActiveAccounts() {
        return activeAccounts;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        PlayerAccount account = getAccount(p.getName());
        if (account == null) {
            account = new PlayerAccount(p.getName().toLowerCase(), Utils.playersHided(p.getName()));
            if (account.getHide()) Bukkit.getOnlinePlayers().forEach(player -> p.hidePlayer(WellHide.getPlugin(), player));
            activeAccounts.add(account);
        } else {
            if (account.getHide()) Bukkit.getOnlinePlayers().forEach(player -> p.hidePlayer(WellHide.getPlugin(), player));
        }
        hideForEachWhoHide(p);
    }

    public static PlayerAccount getAccount(String playerName){
        for(PlayerAccount ac : activeAccounts){
            if (ac.getPlayerName().equalsIgnoreCase(playerName)) return ac;
        }
        return null;
    }

    private void hideForEachWhoHide(Player player){
        for(PlayerAccount ac : activeAccounts){
            if(ac.getHide()) {
                Bukkit.getPlayer(ac.getPlayerName()).hidePlayer(WellHide.getPlugin(), player);
            }
        }
    }

}
