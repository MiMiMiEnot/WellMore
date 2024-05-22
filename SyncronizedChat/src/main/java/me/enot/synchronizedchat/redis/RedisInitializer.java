package me.enot.synchronizedchat.redis;

import org.bukkit.Bukkit;

public final class RedisInitializer {


    public static RedisCore init(Object host, Integer port, String password) {
        Bukkit.getConsoleSender().sendMessage("Инициализация Redis подключения с данными: ");
        Bukkit.getConsoleSender().sendMessage("host = " + host);
        Bukkit.getConsoleSender().sendMessage("port = " + port);
        Bukkit.getConsoleSender().sendMessage("password = <обнаружен>");
        Bukkit.getConsoleSender().sendMessage("timeout = 0");
        Bukkit.getConsoleSender().sendMessage("maxRedisConnections = 8");
        return new RedisCore((String) host, port, 0, password, 8);
    }

    public static RedisCore init(Object host, Integer port) {
        Bukkit.getConsoleSender().sendMessage("Инициализация Redis подключения с данными: ");
        Bukkit.getConsoleSender().sendMessage("host = " + host);
        Bukkit.getConsoleSender().sendMessage("port = " + port);
        Bukkit.getConsoleSender().sendMessage("password = <не обнаружен>");
        Bukkit.getConsoleSender().sendMessage("timeout = 0");
        Bukkit.getConsoleSender().sendMessage("maxRedisConnections = 8");
        return new RedisCore((String) host, port, 0, null, 8);
    }

}
