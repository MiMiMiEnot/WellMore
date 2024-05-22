package me.enot.synchronizedchat.subscriber;

import me.enot.synchronizedchat.SynchronizedChat;
import me.enot.synchronizedchat.configurations.Settings;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

public class JedisPublSubsListener implements Runnable {

    @Override
    public void run() {
        try (Jedis jedis = SynchronizedChat.getRedisCore().getJedisPool().getResource()) {
            try {
                JedisPublSubs jsps = SynchronizedChat.getJedisPublSubs();
                Bukkit.getConsoleSender().sendMessage("register " + Settings.getInstance().getChannelName());
                jedis.subscribe(jsps, Settings.getInstance().getChannelName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
