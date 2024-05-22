package me.enot.wellgui.gui.guiitem;


public class GUIItemSend implements GUIItemType {

    private String server;
    private int maxOnline;
    private String bypassPermission;
    private boolean requireVk;

    public GUIItemSend(String server, int maxOnline, String bypassPermission, boolean requireVk){
        this.server = server;
        this.maxOnline = maxOnline;
        this.bypassPermission = bypassPermission;
        this.requireVk = requireVk;
    }

    public String getServer() {
        return server;
    }

    public int getMaxOnline() {
        return maxOnline;
    }

    public String getBypassPermission() {
        return bypassPermission;
    }

    public boolean requireVk() {
        return requireVk;
    }
}
