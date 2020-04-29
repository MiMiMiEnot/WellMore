package me.enot.wellhide.events.util;

import me.enot.wellhide.sql.Utils;

public class PlayerAccount {

    private String playerName;
    private boolean hide;

    public PlayerAccount(String playerName, boolean hide){
        this.playerName = playerName;
        this.hide = hide;
    }

    public PlayerAccount(String playerName){
        this.playerName = playerName;
        this.hide = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean getHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        Utils.update(this.playerName, hide);
        this.hide = hide;
    }
}
