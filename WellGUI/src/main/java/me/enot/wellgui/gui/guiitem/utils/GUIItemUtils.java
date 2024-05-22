package me.enot.wellgui.gui.guiitem.utils;

import me.enot.wellgui.configurations.Settings;
import me.enot.wellgui.configurations.language.Langs;
import me.enot.wellgui.configurations.language.Replace;
import me.enot.wellgui.gui.event.GUIEventsListener;
import me.enot.wellgui.gui.guiitem.GUIItem;
import me.enot.wellgui.gui.guiitem.GUIItemCommand;
import me.enot.wellgui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUIItemUtils {
    private GUIItemUtils(){}

    public static int calculateSlot(int x, int y){
        return (y - 1) * 9 + (x - 1);
    }

    public static ItemStack getStack(Material material, Integer amount, Integer data,
                                     String displayName, List<String> lore, HashMap<Enchantment, Integer> enchants) {
        amount = (amount == null ? 1 : amount);
        data = (data == null ? 0 : data);
        ItemStack stack = new ItemStack(material, amount, data.shortValue());
        if(enchants != null){
            stack.addUnsafeEnchantments(enchants);
        }
        if(displayName != null || lore != null){
            ItemMeta meta = stack.getItemMeta();

            if(displayName != null) meta.setDisplayName(Message.getInstance().toColoredMessage(displayName));
            if(lore != null) meta.setLore(Message.getInstance().toColoredMessage(lore));

            stack.setItemMeta(meta);
        }

        return stack;
    }

    public static ItemStack replaceAllInItem(ItemStack stack, Player player, GUIItem item){
        ItemStack s = stack.clone();
        if(s.hasItemMeta()){
            ItemMeta meta = s.getItemMeta();
            if (Settings.getInstance().hideAttributes()) meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (Settings.getInstance().hideDestroys()) meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            if (Settings.getInstance().hideEnchants()) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            if (Settings.getInstance().hidePlacedOn()) meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            if (Settings.getInstance().hidePotionEffects()) meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            if(meta.hasDisplayName()){
                meta.setDisplayName(replace(meta.getDisplayName(), player));
            }
            if(meta.hasLore()){
                List<String> lore = replace(meta.getLore(), player);
                if(item.getAccessPermission() != null){
                    if(GUIEventsListener.hasPermission(player, item.getAccessPermission())){
                        if(item.getHasAccess() != null) item.getHasAccess().forEach(string ->
                                lore.add(Message.getInstance().toColoredMessage(string, null)));
                    } else {
                        if(item.getNoAccess() != null) item.getNoAccess().forEach(string ->
                                lore.add(Message.getInstance().toColoredMessage(string, null)));
                    }
                }
                meta.setLore(lore);
            } else if(item.getAccessPermission() != null && (item.getHasAccess() != null || item.getNoAccess() != null)){
                if(GUIEventsListener.hasPermission(player, item.getAccessPermission())) {
                    if(item.getHasAccess() != null) meta.setLore(item.getHasAccess());
                } else {
                    if(item.getNoAccess() != null) meta.setLore(item.getNoAccess());
                }
            }
            s.setItemMeta(meta);
        }
        return s;
    }

    private static String replace(String s, Player player) throws NullPointerException{
        String r = s;
        while (r.contains("{online--")) {
            String server = r.substring(r.indexOf("{online--") + 9, r.indexOf("}"));
//            Bukkit.getConsoleSender().sendMessage(server);
//            BungeeCordUtils.getInstance().sendServerOnlineRequest(server);
            Integer online = BungeeCordUtils.serverOnline.get(server);
            if (online == null) {
                r = r.replace("{online--" + server + "}", "");
                break;
            }
//            if (online == null) {
//                Message.getInstance().sendMessage(player, Langs.gui__open__please_wait);
//
//            }
//            Bukkit.getConsoleSender().sendMessage(r);
            r = r.replace("{online--" + server + "}",
                    Integer.toString(online));
//            Bukkit.getConsoleSender().sendMessage(r);
        }
        return r.replace("{player}", player.getName());
    }
    private static List<String> replace(List<String> list, Player player){
//        list.forEach(string -> replace(string, player));
        List<String> returnList = new ArrayList<>();

        list.forEach(string -> returnList.add(replace(string, player)));

        return returnList;
    }
}
