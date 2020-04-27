package me.enot.wellchat.chat;

import me.enot.wellchat.configurations.Settings;
import me.enot.wellchat.configurations.language.Langs;
import me.enot.wellchat.configurations.language.Replace;
import me.enot.wellchat.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatUtils {

    public static String withoutBedWords(String message, Player p){
        List<String> list = Settings.getInstance().getBlockedWords();
        int i = 0;
        String ret = " " + message + " ";
        for(String s : list) {
            if (ret.toLowerCase().contains(s.toLowerCase())) {
                i++;
                ret = ret.toLowerCase().replaceAll(s.toLowerCase(), getRandomGoodMessage());
                Bukkit.getConsoleSender().sendMessage(ret);
            }
        }
        if (i > 0) Bukkit.broadcast(Message.getInstance().getMessage(Langs.advertisement__broadcast_to_moderators,
                new Replace("\\{name\\}", p.getName()),
                new Replace("\\{message\\}", message)), Settings.getInstance().getPermissionSeeOriginal());
        Bukkit.getConsoleSender().sendMessage(ret);
        if(ret.startsWith(" ")) ret = ret.substring(1);
        Bukkit.getConsoleSender().sendMessage(ret);
        if(ret.endsWith(" ")) ret = ret.substring(ret.length() - 1);
        Bukkit.getConsoleSender().sendMessage("Без матов: " + ret);
        return ret;
    }

    private static String getRandomGoodMessage(){
        String s = Settings.getInstance().getReplaceTo().get(randomNumber(0, Settings.getInstance().getReplaceTo().size() - 1));
        Bukkit.getConsoleSender().sendMessage(s);
        return s;
    }
    private static int randomNumber(int minimum, int maximum){
        double random = Math.random();
        maximum -= minimum;
        int rand = (int) (random * ++maximum)+ minimum;
        Bukkit.getConsoleSender().sendMessage(random + " " + maximum + " " + rand);
        return rand;
    }

}
