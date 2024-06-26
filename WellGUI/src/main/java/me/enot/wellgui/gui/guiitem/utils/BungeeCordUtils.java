package me.enot.wellgui.gui.guiitem.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.enot.wellgui.WellGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;

public class BungeeCordUtils implements PluginMessageListener {

    protected static class BungeeCordUtilsHolder{
        public static final BungeeCordUtils INSTANCE = new BungeeCordUtils();
    }

    public static BungeeCordUtils getInstance(){
        return BungeeCordUtilsHolder.INSTANCE;
    }

    public static HashMap<String, Integer> serverOnline = new HashMap<>();

//    public HashMap<String, Integer> getServerOnline() {
//        return serverOnline;
//    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
//        Bukkit.getConsoleSender().sendMessage("1");
        if(!channel.equals("legacy:redisbungee") && !channel.equals("RedisBungee")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
//        Bukkit.getConsoleSender().sendMessage("received request");
        if(subchannel.equals("PlayerCount")){
//            Bukkit.getConsoleSender().sendMessage("2");
            try {
                String server = in.readUTF();
                int players = in.readInt();
//                Bukkit.getConsoleSender().sendMessage("PlayerCount " + server + " " + players);
                if (serverOnline.containsKey(server)) {
                    serverOnline.replace(server, players);
                } else {
                    serverOnline.put(server, players);
                }
            } catch (IllegalStateException ignored) {}
//            Bukkit.getConsoleSender().sendMessage("" + serverOnline.get(server));
        }
    }

//    public void sendServerOnlineRequest(String server){
//        ByteArrayDataOutput out = ByteStreams.newDataOutput();
//        out.writeUTF("PlayerCount");
//        out.writeUTF(server);
//
//        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
//
//        player.sendPluginMessage(WellGUI.getPlugin(), "BungeeCord", out.toByteArray());
//        Bukkit.getConsoleSender().sendMessage("Send request");
//    }

    public void sendPlayerToServer(Player player, String server){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(WellGUI.getPlugin(), "BungeeCord", out.toByteArray());
//        Bukkit.getConsoleSender().sendMessage("Send player to " + server);
    }

}
