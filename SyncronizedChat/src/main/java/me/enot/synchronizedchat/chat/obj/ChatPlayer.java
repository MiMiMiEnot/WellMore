package me.enot.synchronizedchat.chat.obj;

import com.google.common.collect.Lists;
import me.enot.synchronizedchat.SynchronizedChat;
import me.enot.synchronizedchat.configurations.Settings;
import me.enot.synchronizedchat.data.DataBaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ChatPlayer {

    private String playerName;
    private List<ChatEntry> chatHistory;
    private Set<String> ignored;

    public ChatPlayer(String playerName) {
        this.playerName = playerName;
        this.chatHistory = Lists.newArrayList();
        this.ignored = DataBaseUtil.ignoreList(SynchronizedChat.getMySQL(), this.playerName);
        if (this.ignored == null) this.ignored = new HashSet<>();
        this.ignored.remove(playerName);
    }

    public CompletableFuture<Void> addChatEntry(ChatEntry newChatEntry) {
        return CompletableFuture.supplyAsync(() -> {
            this.chatHistory.add(newChatEntry);
            return null;
        });
    }

    public CompletableFuture<Void> cleanUpChatHistory() {
        return CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < this.chatHistory.size(); i++) {
                if (this.chatHistory.get(i).getTime() <= (System.currentTimeMillis() - Settings.getInstance().getHistoryCleanUp())) {
                    this.chatHistory.remove(i);
                }
            }
            return null;
        });
    }

    public CompletableFuture<ChatEntry[]> chatEntryHistoryForLast(int millis) {
        return CompletableFuture.supplyAsync(() -> {
           return (ChatEntry[]) this.chatHistory.stream().filter(ce -> ce.getTime() >= (System.currentTimeMillis() - millis)).toArray();
        });
    }

    public List<ChatEntry> getChatHistory() {
//        this.chatHistory.forEach(ce -> Bukkit.getConsoleSender().sendMessage("ce | " + ce.getMessage() + " | " + ce.getTime()));
        return chatHistory;
    }

    public boolean isIgnored(String playerName) {
        return this.ignored.stream().anyMatch(s -> s.equalsIgnoreCase(playerName));
    }

//    public Set<String> getNotIgnored(Collection<? extends Player> players) {
//        Set<String> notIgnored
//    }

    public String getPlayerName() {
        return playerName;
    }

    public Set<String> getIgnored() {
        return ignored;
    }
}