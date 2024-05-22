package me.enot.synchronizedchat;

import me.enot.synchronizedchat.chat.listener.ChatFormatter;
import me.enot.synchronizedchat.commands.ChatCommand;
import me.enot.synchronizedchat.commands.IgnoreCommand;
import me.enot.synchronizedchat.commands.PrivateMessageCommand;
import me.enot.synchronizedchat.commands.ReplyCommand;
import me.enot.synchronizedchat.configurations.Settings;
import me.enot.synchronizedchat.configurations.language.Language;
import me.enot.synchronizedchat.data.MySQL;
import me.enot.synchronizedchat.redis.RedisCore;
import me.enot.synchronizedchat.redis.RedisInitializer;
import me.enot.synchronizedchat.subscriber.JedisPublSubs;
import me.enot.synchronizedchat.subscriber.JedisPublSubsListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SynchronizedChat extends JavaPlugin {
    public static Plugin getPlugin() {return SynchronizedChat.getPlugin(SynchronizedChat.class);}

    private static RedisCore redisCore;
    private static JedisPublSubs jedisPublSubs;
    private static Thread jpslThread;
    private static MySQL mySQL;

    @Override
    public void onEnable() {
        Settings.getInstance().reload();
        Language.getInstance().reload();

        redisCore = Settings.getInstance().getRedisPassword().equals("")
                ?
                RedisInitializer.init(Settings.getInstance().getRedisHost(), Settings.getInstance().getRedisPort())
                :
                RedisInitializer.init(Settings.getInstance().getRedisHost(), Settings.getInstance().getRedisPort(), Settings.getInstance().getRedisPassword());
//        Bukkit.getConsoleSender().sendMessage("Статус подключения к Redis: " + redisCore.ping());
        jedisPublSubs = new JedisPublSubs();
        JedisPublSubsListener listener = new JedisPublSubsListener();
        jpslThread = new Thread(listener);
        jpslThread.start();

        mySQL = new MySQL();
        Bukkit.getPluginManager().registerEvents(new ChatFormatter(), this);
        ChatCommand chatCommand = new ChatCommand();
        getCommand("chat").setExecutor(chatCommand);
        getCommand("chat").setTabCompleter(chatCommand);
        IgnoreCommand ignoreCommand = new IgnoreCommand();
        getCommand("ignore").setExecutor(ignoreCommand);
//        getCommand("ignore").setTabCompleter(ignoreCommand);
        getCommand("privatemessage").setExecutor(new PrivateMessageCommand());
        getCommand("reply").setExecutor(new ReplyCommand());
    }

    @Override
    public void onDisable() {
        if (mySQL != null) {
            mySQL.close();
        }
        jpslThread.interrupt();
        if (redisCore.getJedisPool() != null) {
            Bukkit.getConsoleSender().sendMessage("Выключаю пул подключений к Redis");
            redisCore.getJedisPool().close();
        } else {
            Bukkit.getConsoleSender().sendMessage("Пул подключений не был установлен корректно");
        }
    }

    public static Thread getJpslThread() {
        return jpslThread;
    }

    public static JedisPublSubs getJedisPublSubs() {
        return jedisPublSubs;
    }

    public static RedisCore getRedisCore() {
        return redisCore;
    }

    public static MySQL getMySQL() {
        return mySQL;
    }
}