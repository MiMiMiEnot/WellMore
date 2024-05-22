package me.enot.synchronizedchat.subscriber;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.enot.synchronizedchat.configurations.Settings;
import me.enot.synchronizedchat.subscriber.obj.BadWordAddObject;
import me.enot.synchronizedchat.subscriber.obj.BadWordRemoveObject;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;

public class JedisPublSubs extends JedisPubSub {
    private final Gson gson = new Gson();
    @Override
    public void onMessage(String channel, String message) {
        JsonObject jobj = validObject(message);
        if (jobj == null) {
            Bukkit.getConsoleSender().sendMessage("Получено не json сообщение: " + message);
            return;
        }
        String type = jobj.get("type").getAsString();
        switch (type) {
            case "BadWordAddObject":
                BadWordAddObject badWordAddObject = gson.fromJson(message, BadWordAddObject.class);
                Bukkit.getConsoleSender().sendMessage("Добавление " + message);
                if (!Settings.getInstance().getBadWords().contains(badWordAddObject.getBadWord())) {
                    Settings.getInstance().getBadWords().add(badWordAddObject.getBadWord());
                }
                break;
            case "BadWordRemoveObject":
                BadWordRemoveObject badWordRemoveObject = gson.fromJson(message, BadWordRemoveObject.class);
                Bukkit.getConsoleSender().sendMessage("Удаление " + message);
                if (Settings.getInstance().getBadWords().contains(badWordRemoveObject.getBadWord())) {
                    Settings.getInstance().getBadWords().remove(badWordRemoveObject.getBadWord());
                }
                break;
            default:
                Bukkit.getConsoleSender().sendMessage("Плохое сообщение на " + channel + ": " + message);
                break;
        }
        Settings.getInstance().saveBadWords();
        Settings.getInstance().reload();
    }

    private JsonObject validObject(String json) {
        try {
            return gson.fromJson(json, JsonObject.class);
        } catch (Exception ignored) {}
        return null;
    }

}