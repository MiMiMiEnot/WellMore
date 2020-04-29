package me.enot.wellgui.gui.serializable;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import me.enot.wellgui.configurations.Settings;
import me.enot.wellgui.configurations.language.Langs;
import me.enot.wellgui.configurations.language.Replace;
import me.enot.wellgui.gui.GUI;
import me.enot.wellgui.gui.guiitem.*;
import me.enot.wellgui.gui.guiitem.utils.Executor;
import me.enot.wellgui.gui.guiitem.utils.GUIItemLogicType;
import me.enot.wellgui.gui.guiitem.utils.GUIItemUtils;
import me.enot.wellgui.gui.serializable.exception.RequiredPathNotFoundException;
import me.enot.wellgui.gui.serializable.exception.SlotInvalidException;
import me.enot.wellgui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class Serialization {

    private static final String TITLE = "title";
    private static final String COMMAND = "command";
    private static final String ROWS = "rows";

    private static final String ITEMS = "items.";

    private static final String MATERIAL = ".material";
    private static final String SLOTS_X = ".slots.X";
    private static final String SLOTS_Y = ".slots.Y";
    private static final String AMOUNT = ".amount";
    private static final String DATA = ".data";
    private static final String DISPLAY_NAME = ".item-meta.display-name";
    private static final String LORE = ".item-meta.lore";
    private static final String ENCHANTMENTS = ".enchantments.";
    private static final String TYPE = ".item-logic.type";

    private static final String EXECUTOR = ".item-logic.executor";
    private static final String COMMANDS = ".item-logic.commands";

    private static final String ID = ".item-logic.id";

    private static final String SERVER = ".item-logic.server";
    private static final String MAX_ONLINE = ".item-logic.max-online";
    private static final String BYPASS_PERMISSION = ".item-logic.bypass-permission";

    private static final String ACCESS_PERMISSION = ".access-permission";
    private static final String NO_ACCESS = ".no-access";
    private static final String HAS_ACCESS = ".has-access";
    private static final String NO_ACCESS_MESSAGE = ".no-access-message";
    private static final String HAS_ACCESS_MESSAGE = ".has-access-message";

    public static List<GUI> guis = new ArrayList<>();

    public static void load(){
        Long time = System.currentTimeMillis();
        File[] guiFiles = Settings.getInstance().GUI_DIR.listFiles();
        for(File f : guiFiles) {
            try {
                guis.add(load(f));
            } catch (RequiredPathNotFoundException | SlotInvalidException e){
                e.printStackTrace();
            }
        }
        Long newTime = System.currentTimeMillis() - time;
        Message.getInstance().sendMessage(Bukkit.getConsoleSender(), Langs.main__load__load_guis,
                new Replace("\\{X\\}", Integer.toString(guis.size())),
                new Replace("\\{Y\\}", Long.toString(newTime)));
    }

    public static GUI load(File f) throws RequiredPathNotFoundException, SlotInvalidException {
        if(f.getName().endsWith(".conf")){
            Config c = ConfigFactory.parseFile(f);

            if(isValidGUISettingsConfig(c)) {

                String title = c.getString(TITLE);
                String command = c.getString(COMMAND);
                int rows = c.getInt(ROWS);

                List<String> keys = new ArrayList<>();
                for (Map.Entry<String, ConfigValue> entry : c.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith(ITEMS)) {
                        String splited = key.split("\\.")[1];
                        if (keys.contains(splited)) continue;
                        keys.add(splited);
                    }
                }

                List<GUIItem> list = new ArrayList<>();

                for(String s : keys){
                    if(isValidGUIItemConfig(c, s)){
                        list.add(loadItemFromConfigByKey(c, s));
                    }
                }
                return new GUI(getGUIIdByConfig(c), list, Message.getInstance().toColoredMessage(title), command, rows);
            }
        }
        return null;
    }

    public static String getGUIIdByConfig(Config c){
        String[] var1 = c.origin().filename().split(File.separator);
        Bukkit.getConsoleSender().sendMessage(var1);
        String var2 = var1[var1.length - 1].split("\\.")[0];
        Bukkit.getConsoleSender().sendMessage(var2);
        return var2;
    }

    public static GUIItem loadItemFromConfigByKey(Config config, String key) throws SlotInvalidException {
        String string = ITEMS + key;
        Material material = config.getEnum(Material.class, string + MATERIAL);
        Integer amount = config.hasPath(string + AMOUNT) ? config.getInt(string + AMOUNT) : null;
        Integer data = config.hasPath(string + DATA) ? config.getInt(string + DATA) : null;
        String displayName = config.hasPath(string + DISPLAY_NAME) ? config.getString(string + DISPLAY_NAME) : null;
        List<String> lore = config.hasPath(string + LORE) ? config.getStringList(string + LORE) : null;
        HashMap<Enchantment, Integer> enchants = loadEnchMaps(config, key);
        ItemStack stack = GUIItemUtils.getStack(material, amount, data, displayName, lore, enchants);
        int slot = GUIItemUtils.calculateSlot(config.getInt(string + SLOTS_X), config.getInt(string + SLOTS_Y));
        GUIItemType guiItemType = loadGUIItemType(config, key);
        String accessPermission = config.hasPath(string + ACCESS_PERMISSION) ? config.getString(string + ACCESS_PERMISSION) : null;
        List<String> noAccess = loadListByAnything(config, string + NO_ACCESS);
        List<String> hasAccess = loadListByAnything(config, string + HAS_ACCESS);
        List<String> noAccessMessage = loadListByAnything(config, string + NO_ACCESS_MESSAGE);
        List<String> hasAccessMessage = loadListByAnything(config, string + HAS_ACCESS_MESSAGE);

        int maxSlots = config.getInt("rows") * 9;
        if(maxSlots < slot || slot < 0) throw new SlotInvalidException(key, slot, config);

        return new GUIItem(key, stack, slot, guiItemType, accessPermission, noAccess, hasAccess, noAccessMessage, hasAccessMessage);
    }

    private static HashMap<Enchantment, Integer> loadEnchMaps(Config c, String key){
        HashMap<Enchantment, Integer> map = new HashMap<>();
        for(Map.Entry<String, ConfigValue> entry : c.entrySet()){
            if(entry.getKey().startsWith(ITEMS + key + ENCHANTMENTS)) {
                String s = entry.getKey();
                ConfigValue value = entry.getValue();
                try {
                    String[] split = s.split("\\.");
                    Enchantment enchantment = Enchantment.getByName(split[split.length - 1]);
                    int level = (int) value.unwrapped();
                    map.put(enchantment, level);
                } catch (Exception ex){
                    Message.getInstance().sendMessage(Bukkit.getConsoleSender(), Langs.main__load__load_error_enchant,
                            new Replace("\\{E\\}", s.split("\\.")[s.split("\\.").length - 1]),
                            new Replace("\\{EXCEPTION-MESSAGE\\}", ex.getMessage()));
                }
            }
        }
        return map;
    }

    private static GUIItemType loadGUIItemType(Config c, String key){
        String string = ITEMS + key;

        if(c.hasPath(key + TYPE)) {
            switch (c.getEnum(GUIItemLogicType.class, key + TYPE)) {
                case COMMAND:
                    List<String> commands = loadListByAnything(c, key + COMMANDS);
                    Executor executor = c.getEnum(Executor.class, key + EXECUTOR);
                    return new GUIItemCommand(executor, commands);
                case OPEN:
                    String id = c.getString(key + ID);
                    return new GUIItemOpen(id);
                case SEND:
                    String server = c.getString(key + SERVER);
                    int maxOnline = c.getInt(key + MAX_ONLINE);
                    String bypassPermission = c.getString(key + BYPASS_PERMISSION);
                    return new GUIItemSend(server, maxOnline, bypassPermission);
            }
        }
        return null;
    }

    /*private static String replace(String s, Replace... replaces){
        for(Replace replace : replaces){
            s = s.replaceAll(replace.getWhat(), replace.getTo());
        }
        return s;
    }
    private static List<String> replace(List<String> list, Replace... replaces){
        list.forEach(string -> replace(string, replaces));
        return list;
    }*/

    private static List<String> loadListByAnything(Config c, String path){
        if(c.hasPath(path)){
            Object object = c.getValue(path).unwrapped();
            if(object instanceof String){
                return Arrays.asList((String) object);
            } else if(object instanceof List){
                return (List<String>) object;
            }
        }
        return null;
    }

    public static boolean isValidGUISettingsConfig(Config c) throws RequiredPathNotFoundException {
        if(!c.hasPath(TITLE)) throw new RequiredPathNotFoundException(TITLE, c);
        if(!c.hasPath(COMMAND)) throw new RequiredPathNotFoundException(COMMAND, c);
        if(!c.hasPath(ROWS)) throw new RequiredPathNotFoundException(ROWS, c);
        return true;
    }

    public static boolean isValidGUIItemConfig(Config c, String string) throws RequiredPathNotFoundException {
        String key = ITEMS + string;

        if(!c.hasPath(key + MATERIAL)) throw new RequiredPathNotFoundException(key + MATERIAL, c);
        if(!c.hasPath(key + SLOTS_X)) throw new RequiredPathNotFoundException(key + SLOTS_X, c);
        if(!c.hasPath(key + SLOTS_Y)) throw new RequiredPathNotFoundException(key + SLOTS_Y, c);

        if(c.hasPath(key + TYPE)){
            switch (c.getEnum(GUIItemLogicType.class, key + TYPE)){
                case COMMAND:
                    if(!c.hasPath(key + EXECUTOR)) throw new RequiredPathNotFoundException(key + EXECUTOR, c);
                    if(!c.hasPath(key + COMMANDS)) throw new RequiredPathNotFoundException(key + COMMANDS, c);
                    break;
                case OPEN:
                    if(!c.hasPath(key + ID)) throw new RequiredPathNotFoundException(key + ID, c);
                    break;
                case SEND:
                    if(!c.hasPath(key + SERVER)) throw new RequiredPathNotFoundException(key + SERVER, c);
                    if(!c.hasPath(key + MAX_ONLINE)) throw new RequiredPathNotFoundException(key + MAX_ONLINE, c);
                    if(!c.hasPath(key + BYPASS_PERMISSION)) throw new RequiredPathNotFoundException(key + BYPASS_PERMISSION, c);
                    break;
            }
        }
        return true;
    }

    public static GUI getGUIByCommand(String command){
        for(GUI gui : guis){
            if(gui.getCommand().equalsIgnoreCase(command)) return gui;
        }
        return null;
    }
    public static GUI getGUIByID(String id){
        for(GUI gui : guis){
            if(gui.getId().equalsIgnoreCase(id)) return gui;
        }
        return null;
    }

}
