package me.enot.wellhide.api;

import me.enot.wellhide.configurations.Settings;
import me.enot.wellhide.events.PlayerJoin;
import me.enot.wellhide.events.util.MUtils;
import me.enot.wellhide.events.util.PlayerAccount;
import org.bukkit.entity.Player;

import java.util.Set;

public class WellHideAPI {
    private WellHideAPI() {}

    public static Set<PlayerAccount> activePlayers() {
        return PlayerJoin.getActiveAccounts();
    }

    public static void changeOnlineWay(String playerName, boolean value) {
        PlayerAccount account = PlayerJoin.getAccount(playerName);
        if (account != null && account.getHide() != value) {
            account.setHide(value);
        }
    }
    public static void changeMySQLWay(String playerName, boolean value) {
        me.enot.wellhide.sql.Utils.update(playerName, value);
    }

    public static int canChangeHide(String playerName) {
        if(Utils.counter.containsKey(playerName.toLowerCase())) {
            Long time = Utils.counter.get(playerName.toLowerCase());
            int t = (int) ((System.currentTimeMillis() - time) / 1000);
            if (t <= Settings.getInstance().getDelay()) {
                return t;
            }
        }
        return -1;
    }

    public static boolean hidePlayersSetWay(String playerName) {
        PlayerAccount account = PlayerJoin.getAccount(playerName);
        if (account != null) {
            return account.getHide();
        }
        return false;
    }

    public static boolean hidePlayersSQLWay(String playerName){
        return me.enot.wellhide.sql.Utils.playersHided(playerName);
    }

    public static void hidePlayers(Player p) {
        MUtils.hidePlayers(p);
    }
    public static void showPlayers(Player p) {
        MUtils.showPlayers(p);
    }

}
