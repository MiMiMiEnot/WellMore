package me.enot.synchronizedchat.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import me.enot.synchronizedchat.SynchronizedChat;
import me.enot.synchronizedchat.utils.PermissionUtil;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class Settings {

    protected static class SettingsHolder {
        public static final Settings INSTANCE = new Settings();
    }

    public static Settings getInstance(){
        return SettingsHolder.INSTANCE;
    }

    private Plugin plugin = SynchronizedChat.getPlugin();
    private String fileName = "settings.conf";
    private File file = new File(plugin.getDataFolder(), fileName);
    private InputStream is = getClass().getResourceAsStream("/" + fileName);

    private Config settings = null;

    private void create(){
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        if(!file.exists()) {
            try {
                Files.copy(is, file.toPath());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        settings = ConfigFactory.parseFile(file);
        for (PermissionUtil.Perm perm : PermissionUtil.Perm.values()) {
            if (!settings.hasPath(perm.getPath())) {
                settings = settings.withValue(perm.getPath(), ConfigValueFactory.fromAnyRef(perm.getDefaultValue()));
            }
        }
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), settings.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Arrays.stream(PermissionUtil.Perm.values()).forEach(PermissionUtil.Perm::update);
    }

    public Config getSettings() {
        return settings;
    }

    public void reload(){
        if(settings == null){
            create();
        }
        settings = ConfigFactory.parseFile(file);
        load();
    }
    public void saveBadWords() {
        settings = settings.withValue("bad-words", ConfigValueFactory.fromAnyRef(new ArrayList<>(badWords)));
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), settings.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object redisHost;
    private Integer redisPort;
    private String redisPassword;

    private String channelName;

    private Object mysqlHost;
    private int mysqlPort;
    private String mysqlUser;
    private String mysqlPassword;

    private int historyCleanUp;
    private int messageDelay;
    private int maxChars;

    private Set<String> badWords;
    private List<String> replaceWords;

    private String urlPattern;
    private String allowedPattern;

    private boolean linkRequire;
    private boolean localChatEnabled;
    private int localChatRadius;

    private void load() {
        redisHost = settings.getAnyRef("redis.host");
        redisPort = settings.getInt("redis.port");
        redisPassword = settings.getString("redis.password");

        channelName = settings.getString("redis.channel-name");

        mysqlHost = settings.getAnyRef("mysql.host");
        mysqlPort = settings.getInt("mysql.port");
        mysqlUser = settings.getString("mysql.user");
        mysqlPassword = settings.getString("mysql.password");

        historyCleanUp = settings.getInt("history-clean-up");
        messageDelay = settings.getInt("message-delay");
        maxChars = settings.getInt("max-chars");

        badWords = new HashSet<>(settings.getStringList("bad-words"));
        replaceWords = settings.getStringList("replace-words");
        urlPattern = settings.getString("url-pattern");
        allowedPattern = settings.getString("allowed-pattern");
        linkRequire = settings.getBoolean("link-require");

        localChatEnabled = settings.getBoolean("local-chat-enabled");
        localChatRadius = settings.getInt("local-chat-radius");
    }

    public Object getRedisHost() {
        return redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public String getChannelName() {
        return channelName;
    }

    public Object getMysqlHost() {
        return mysqlHost;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public int getHistoryCleanUp() {
        return historyCleanUp;
    }

    public int getMessageDelay() {
        return messageDelay;
    }

    public int getMaxChars() {
        return maxChars;
    }

    public Set<String> getBadWords() {
        return badWords;
    }

    public List<String> getReplaceWords() {
        return replaceWords;
    }

    public String getUrlPattern() {
        return "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
    }

    public String getAllowedPattern() {
        return "[0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZабвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ'` !,.?@#\"$%^&*+№()_-]";
    }

    public boolean isLinkRequire() {
        return linkRequire;
    }

    public boolean isLocalChatEnabled() {
        return localChatEnabled;
    }

    public int getLocalChatRadius() {
        return localChatRadius;
    }
}