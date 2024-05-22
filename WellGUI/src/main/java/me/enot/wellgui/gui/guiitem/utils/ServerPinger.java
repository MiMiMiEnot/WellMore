package me.enot.wellgui.gui.guiitem.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.enot.wellgui.WellGUI;
import me.enot.wellgui.configurations.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class ServerPinger {


    private int schedulerId = -1;
    private BukkitTask runnable = null;


    public void start() {

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (String server : Settings.getInstance().getToPingServer()) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("PlayerCount");
                    out.writeUTF(server);

                    Player player = Bukkit.getOnlinePlayers().stream().findAny().get();

                    player.sendPluginMessage(WellGUI.getPlugin(), "legacy:redisbungee", out.toByteArray());
//                    Bukkit.getConsoleSender().sendMessage("Send request for " + server);
                }
//                BungeeCordUtils.serverOnline.forEach((s, i) -> Bukkit.getConsoleSender().sendMessage(s + " " + i));
            }
        }.runTaskTimer(WellGUI.getPlugin(), 0L, Settings.getInstance().getPingEvery() * 20L);

//        this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(WellGUI.getPlugin(), () -> {
//            for (String server : Settings.getInstance().getToPingServer()) {
//                ByteArrayDataOutput out = ByteStreams.newDataOutput();
//
//                out.writeUTF("PlayerCount");
//                out.writeUTF(server);
//
//                Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
//
//                player.sendPluginMessage(WellGUI.getPlugin(), "BungeeCord", out.toByteArray());
//                Bukkit.getConsoleSender().sendMessage("Send request for " + server);
//            }
//            BungeeCordUtils.getInstance().getServerOnline().forEach((s, i) -> Bukkit.getConsoleSender().sendMessage(s + " " + i));
//        }, 0, Settings.getInstance().getPingEvery() * 1000);
    }

    public void stop() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
        //        if (schedulerId != -1)
//            Bukkit.getScheduler().cancelTask(schedulerId);
    }

    public void restart() {
        this.stop();
        this.start();
    }

}
