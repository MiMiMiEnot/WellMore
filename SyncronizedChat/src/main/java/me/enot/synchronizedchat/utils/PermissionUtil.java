package me.enot.synchronizedchat.utils;

import me.enot.synchronizedchat.configurations.Settings;
import org.bukkit.entity.Player;

public final class PermissionUtil {

    public enum Perm {

        CHAT_BYPASS("chat.bypass"),
        CHAT_BROADCAST("chat.broadcast"),
        COMMANDS__CHAT__RELOAD("chat.commands.reload"),
        COMMANDS__CHAT__TEST("chat.commands.test"),
        COMMANDS__CHAT__USAGE("chat.commands.chat"),
        COMMANDS__CHAT__ADD("chat.commands.add"),
        COMMANDS__CHAT__REMOVE("chat.commands.remove"),
        COMMANDS__CHAT__HISTORY("chat.commands.history"),

        ;

        private String path;
        private String defaultValue;
        private String currentValue;
        Perm(String defaultValue) {
            this.path = "permissions." + this.toString().replace("__", ".").replace("_", "-").toLowerCase();
            this.defaultValue = defaultValue;
            this.currentValue = defaultValue;
        }

        public String getPath() {
            return path;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getCurrentValue() {
            return currentValue;
        }

        public void update() {
            this.currentValue = Settings.getInstance().getSettings().getString(getPath());
        }

        public boolean has(Player p) {
            return p.hasPermission(this.currentValue);
        }
    }

}