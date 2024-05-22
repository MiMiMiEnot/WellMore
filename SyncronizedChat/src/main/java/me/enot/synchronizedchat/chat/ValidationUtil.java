package me.enot.synchronizedchat.chat;

import com.google.common.collect.Sets;
import me.enot.synchronizedchat.chat.listener.ChatFormatter;
import me.enot.synchronizedchat.chat.obj.ChatEntry;
import me.enot.synchronizedchat.chat.obj.ChatPlayer;
import me.enot.synchronizedchat.chat.obj.util.Reject;
import me.enot.synchronizedchat.chat.obj.util.Warning;
import me.enot.synchronizedchat.configurations.Settings;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private ValidationUtil() {}


    public static void fullValidate(ChatPlayer player, ChatEntry entry) {
        Set<Warning> warnings = Sets.newHashSet(entry.getWarnings());
        Set<Reject> rejects = Sets.newHashSet(entry.getRejects());
        if (player.getChatHistory().size() > 0) {
            player.getChatHistory().forEach(ce -> {
                if (ce == null || entry == null || ce.getMessage() == null || entry.getMessage() == null) {
//                    Bukkit.getConsoleSender().sendMessage("ce == " + (ce == null));
//                    Bukkit.getConsoleSender().sendMessage("ce#message == " + (ce.getMessage() == null));
//                    Bukkit.getConsoleSender().sendMessage("entry#message == " + (entry.getMessage() == null));

                } else
                if (ce.getMessage().equalsIgnoreCase(entry.getMessage())) {
                    rejects.remove(Reject.NONE);
                    rejects.add(Reject.SIMILAR);
                }
            });
            ChatEntry lastEntry = player.getChatHistory().get(player.getChatHistory().size() - 1);
            if (lastEntry != null) {
                if (lastEntry.getTime() > (entry.getTime() - Settings.getInstance().getMessageDelay())) {
                    rejects.remove(Reject.NONE);
                    rejects.add(Reject.TO_QUICK);
                }
            }
        }
        Pattern pattern = Pattern.compile(Settings.getInstance().getUrlPattern());
        Matcher matcher = pattern.matcher(entry.getMessage());
        boolean m = matcher.find() || matcher.matches() || matcher.lookingAt();
        String mes = entry.getEditedMessage() == null ? entry.getMessage() : entry.getEditedMessage();
        if (m) {
            mes = matcher.replaceAll("***");
            entry.setEditedMessage(mes);
            warnings.remove(Warning.NONE);
            warnings.add(Warning.LINKS);
        }
        if (entry.getMessage().length() > Settings.getInstance().getMaxChars()) {
            rejects.remove(Reject.NONE);
            rejects.add(Reject.MAX_CHARS);
        }
        Pattern pattern1 = Pattern.compile(Settings.getInstance().getAllowedPattern());
        Matcher matcher1 = pattern1.matcher(mes);
        if (!"".equals(matcher1.replaceAll(""))) {
            rejects.remove(Reject.NONE);
            rejects.add(Reject.BAD_SYMBOLS);
        }
        Random random = new Random();
        AtomicReference<String> res = new AtomicReference<>(mes.toLowerCase().replace(" ", ""));
        Settings.getInstance().getBadWords().forEach(s -> {
            boolean find = false;
            if (res.get().contains(s)) {
                find = true;
                res.set(res.get().replace(s, Settings.getInstance().getReplaceWords().get(random.nextInt(Settings.getInstance().getReplaceWords().size()))));
                entry.setEditedMessage(res.get());
            }
            if (find) {
                warnings.remove(Warning.NONE);
                warnings.add(Warning.BAD_WORDS);
            }
        });

        if (Settings.getInstance().isLinkRequire() && ChatFormatter.linked.contains(player.getPlayerName())) {
            rejects.remove(Reject.NONE);
            rejects.add(Reject.WITHOUT_LINK);
        }
        entry.setRejects(rejects.toArray(new Reject[0]));
        entry.setWarnings(warnings.toArray(new Warning[0]));
    }

    public static void fullValidateTest(ChatEntry entry) {
        Set<Warning> warnings = Sets.newHashSet(entry.getWarnings());
        Set<Reject> rejects = Sets.newHashSet(entry.getRejects());
        Pattern pattern = Pattern.compile(Settings.getInstance().getUrlPattern());
        Matcher matcher = pattern.matcher(entry.getMessage());
        boolean m = matcher.find() || matcher.matches() || matcher.lookingAt();
        String mes = entry.getEditedMessage() == null ? entry.getMessage() : entry.getEditedMessage();
        if (m) {
            mes = matcher.replaceAll("***");
            entry.setEditedMessage(mes);
            warnings.remove(Warning.NONE);
            warnings.add(Warning.LINKS);
        }
        if (entry.getMessage().length() > Settings.getInstance().getMaxChars()) {
            rejects.remove(Reject.NONE);
            rejects.add(Reject.MAX_CHARS);
        }
        Pattern pattern1 = Pattern.compile(Settings.getInstance().getAllowedPattern());
        Matcher matcher1 = pattern1.matcher(mes);
        if (!"".equals(matcher1.replaceAll(""))) {
            rejects.remove(Reject.NONE);
            rejects.add(Reject.BAD_SYMBOLS);
        }
        Random random = new Random();
        AtomicReference<String> res = new AtomicReference<>(mes.toLowerCase().replace(" ", ""));
        Settings.getInstance().getBadWords().forEach(s -> {
            boolean find = false;
            if (res.get().contains(s)) {
                find = true;
                res.set(res.get().replace(s, Settings.getInstance().getReplaceWords().get(random.nextInt(Settings.getInstance().getReplaceWords().size()))));
                entry.setEditedMessage(res.get());
            }
            if (find) {
                warnings.remove(Warning.NONE);
                warnings.add(Warning.BAD_WORDS);
            }
        });

        entry.setRejects(rejects.toArray(new Reject[0]));
        entry.setWarnings(warnings.toArray(new Warning[0]));
    }

    public static void validateURL(ChatEntry entry) {
        Pattern pattern = Pattern.compile(Settings.getInstance().getUrlPattern());
        Matcher matcher = pattern.matcher(entry.getMessage());
        boolean m = matcher.find() || matcher.matches() || matcher.lookingAt();
        String mes = entry.getEditedMessage() == null ? entry.getMessage() : entry.getEditedMessage();
        if (m) {
            matcher.replaceAll(mes);
            entry.setEditedMessage(mes);
            List<Warning> warnings = Arrays.asList(entry.getWarnings().clone());
            warnings.remove(Warning.NONE);
            warnings.add(Warning.LINKS);
            entry.setWarnings(warnings.toArray(new Warning[0]));
        }
    }

    public static void similar(ChatPlayer player, ChatEntry entry) {
        player.getChatHistory().forEach(ce -> {
            if (ce.getMessage().equalsIgnoreCase(entry.getMessage())) {
                List<Reject> rejects = Arrays.asList(entry.getRejects().clone());
                rejects.remove(Reject.NONE);
                rejects.add(Reject.SIMILAR);
                entry.setRejects(rejects.toArray(new Reject[0]));
            }
        });
    }

    public static void toQuick(ChatPlayer player, ChatEntry entry) {
        if (player.getChatHistory().get(player.getChatHistory().size() - 1).getTime() >= (entry.getTime() - Settings.getInstance().getMessageDelay())) {
            List<Reject> rejects = Arrays.asList(entry.getRejects().clone());
            rejects.remove(Reject.NONE);
            rejects.add(Reject.TO_QUICK);
            entry.setRejects(rejects.toArray(new Reject[0]));
        }
    }

    public static void maxChars(ChatEntry entry) {
        if (entry.getMessage().length() > Settings.getInstance().getMaxChars()) {
            List<Reject> rejects = Arrays.asList(entry.getRejects().clone());
            rejects.remove(Reject.NONE);
            rejects.add(Reject.MAX_CHARS);
            entry.setRejects(rejects.toArray(new Reject[0]));
        }
    }


}
