package me.enot.synchronizedchat.chat.obj;

import me.enot.synchronizedchat.chat.obj.util.Reject;
import me.enot.synchronizedchat.chat.obj.util.Warning;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class ChatEntry {

    private String message;
    private String editedMessage;
    private long time;
    private Warning[] warnings;
    private Reject[] rejects;

    public ChatEntry(String message, long time) {
        this.message = message;
        this.editedMessage = null;
        this.time = time;
        this.warnings = new Warning[]{Warning.NONE};
        this.rejects = new Reject[]{Reject.NONE};
    }

    public ChatEntry(String message) {
        this(message, System.currentTimeMillis());
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String getEditedMessage() {
        return editedMessage == null ? message : editedMessage;
    }

    public Warning[] getWarnings() {
        return warnings;
    }

    public void setEditedMessage(String editedMessage) {
        this.editedMessage = editedMessage;
    }

    public void setWarnings(Warning[] warnings) {
        this.warnings = warnings;
    }

    public Reject[] getRejects() {
        return rejects;
    }

    public void setRejects(Reject[] rejects) {
        this.rejects = rejects;
    }

    public CompletableFuture<Void> validate(Player player) {
        return CompletableFuture.supplyAsync(() -> {
//            if ()

            return null;
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatEntry chatEntry = (ChatEntry) o;

        if (time != chatEntry.time) return false;
        return message != null ? message.equals(chatEntry.message) : chatEntry.message == null;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}