package me.enot.synchronizedchat.utils;

import com.typesafe.config.Config;
import me.enot.synchronizedchat.configurations.language.Langs;
import me.enot.synchronizedchat.configurations.language.Language;
import me.enot.synchronizedchat.configurations.language.Replace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private static class MESSAGE_HOLDER {
        public static final Message INSTANCE = new Message();
    }

    public static Message getInstance(){
        return MESSAGE_HOLDER.INSTANCE;
    }

    public void sendMessage(Player player, Langs langs, Replace... replaces) {
        Object object = getMessage(langs);
        if (object instanceof String){
            String s = (String) object;
            player.sendMessage(toColoredMessage(s, replaces));
        } else if (object instanceof List){
            List<String> list = toColoredMessage((List<String>) object, replaces);
            list.forEach(player::sendMessage);
        } else {
            Bukkit.getConsoleSender().sendMessage("Тип " + langs.convert() + " не удалось определить");
        }
    }
    public void sendMessage(ConsoleCommandSender ccs, Langs langs, Replace... replaces) {
        Object object = getMessage(langs);
        if (object instanceof String){
            String s = (String) object;
            ccs.sendMessage(toColoredMessage(s, replaces));
        } else if (object instanceof List){
            List<String> list = toColoredMessage((List<String>) object, replaces);
            list.forEach(ccs::sendMessage);
        } else {
            Bukkit.getConsoleSender().sendMessage("Тип " + langs.convert() + " не удалось определить");
        }
    }
    public void sendMessage(CommandSender cs, Langs langs, Replace... replaces) {
        Object object = getMessage(langs);
        if (object instanceof String){
            String s = (String) object;
            cs.sendMessage(toColoredMessage(s, replaces));
        } else if (object instanceof List){
            List<String> list = toColoredMessage((List<String>) object, replaces);
            list.forEach(cs::sendMessage);
        } else {
            Bukkit.getConsoleSender().sendMessage("Тип " + langs.convert() + " не удалось определить");
        }
    }

    public Object getMessage(Langs langs) {
        String path = langs.convert();
        Config c = Language.getInstance().getLanguage();
        if (c.hasPath(path)) {
            return c.getValue(path).unwrapped();
        } else {
            return langs.getDefault();
        }
    }

    public String getMessage(Langs langs, Replace... replaces) {
        String s = (String) getMessage(langs);
        if(replaces != null) {
            for (Replace r : replaces) {
                s = s.replace(r.getWhat(), r.getTo());
            }
        }
        return s;
    }

    public String getMessageString(Langs langs) {
        return getMessageString(langs, null);
    }

    public List<String> getMessageList(Langs langs) {
        return getMessageList(langs, null);
    }

    public String getMessageString(Langs langs, Replace... replaces){
        Object object = getMessage(langs);
        if (object instanceof String) {
            String s = (String) object;
            return toColoredMessage(s, replaces);
        } else {
            return null;
        }
    }

    public List<String> getMessageList(Langs langs, Replace... replaces){
        Object object = getMessage(langs);
        if (object instanceof List) {
            List<String> s = (List<String>)object;
            return toColoredMessage(s, replaces);
        } else {
            return null;
        }
    }

    public void broadcastMessage(Langs langs, Replace... replaces) {
        Object object = getMessage(langs);
        if (object instanceof String) {
            Bukkit.broadcastMessage(toColoredMessage((String) object, replaces));
        } else if (object instanceof List) {
            List<String> list = (List<String>) object;
            for (String s : list) {
                Bukkit.broadcastMessage(toColoredMessage((String) s, replaces));
            }
        }
    }

    public void broadcastMessage(Langs langs) {
        broadcastMessage(langs, null);
    }

    public void broadcastPermissionMessage(Langs langs, String permission, Replace... replaces) {
        Object object = getMessage(langs);
        if (object instanceof String) {
            Bukkit.broadcast(toColoredMessage(getMessageString(langs), replaces), permission);
        } else if (object instanceof List) {
            List<String> list = (List<String>) object;
            list.forEach(s -> Bukkit.broadcast(toColoredMessage(s, replaces), permission));
        }
    }

    public void broadcastPermissionMessage(Langs langs, String permission) {
        broadcastPermissionMessage(langs, permission, null);
    }


    public String toColoredMessage(String s, Replace... replaces){
        if(replaces != null) {
            for (Replace r : replaces) {
                s = s.replace(r.getWhat(), r.getTo());
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public List<String> toColoredMessage(List<String> list, Replace... replaces){
        /*if(replaces != null){
            for (int i = 0; i < list.size(); i++){
                list.set(i, toColoredMessage(list.get(i), replaces));
            }
        }*/
        List<String> strings = new ArrayList<>();
        list.forEach(string -> strings.add(toColoredMessage(string, replaces)));
        return strings;
    }
}