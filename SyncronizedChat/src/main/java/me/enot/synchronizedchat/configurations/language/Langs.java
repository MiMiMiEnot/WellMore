package me.enot.synchronizedchat.configurations.language;

import java.util.Arrays;
import java.util.List;

public enum Langs {

    chat__prefix__local("&8[&7L&8]&r "),
    chat__prefix__global("&f[&eG&f]&r "),
    chat__prefix__spy("&6SPY&r "),
    chat__format__without_suffix("{prefix} %1$s &8➯&f %2$s"),
    chat__format__with_suffix("{prefix} %1$s {suffix} &8➯&f %2$s"),

    mod__warning_placeholder("Внимание"),
    mod__reject_placeholder("Блок"),

    mod__warning("&e{warning} &7|&f {username}&7:&f {message}"),
    mod__reject("&c{reject} &7|&f {username}&7:&f {message}"),
    mod__warnings_private_message("&e{warning} &7|&f {sender}->{receiver}&7:&f {message}"),
    mod__reject_private_message("&c{reject} &7|&f {sender}->{receiver}&7:&f {message}"),

    commands__without_permission("&cНет прав"),
    commands__chat__usage("&fИспользуйте: &3/chat [reload/test/add/remove/history] <аргументы>"),
    commands__chat__reload__reloaded("&aКонфиги перезагружены"),
    commands__chat__test__testing(
            "&fТест сообщения: {original}",
            "&fПричины для блокировки: &c{rejects}",
            "&fПредупреждения: &e{warnings}",
            "&fОтредактированное сообщение: {edited}"),
    commands__chat__test__usage("&fИспользуйте: &3/chat test <сообщения для тестирования>"),

    commands__chat__add__usage("&fИспользуйте: &3/chat add <слово что нужно добавить в запрещённые>"),
    commands__chat__add__successful("&fСлово &3{word} &fдобавлено в запрещённые"),
    commands__chat__add__already("&fСлово &3{word} &fуже добавлено в запрещённые. Добавление второй раз запрещено"),

    commands__chat__remove__usage("&fИспользуйте: &3/chat remove <слово что нужно удалить>"),
    commands__chat__remove__successful("&fСлово &3{word} &fудалено с списка запрещённых"),
    commands__chat__remove__already("&fСлово &3{word} &fне найдено. Невозможно удалить"),

    commands__chat__history__usage("&fИспользуйте: &3/chat history [ник] <количество>"),
    commands__chat__history__empty("&fИстория для игрока &3{username} &fне найдена"),
    commands__chat__history__title("&fПоследние &3{X} &fсообщений игрока &3{username}&f:"),
    commands__chat__history__entry("    &f- &3{time} &f| &f{original}"),

    commands__ignore__usage("&fИспользуйте: &3/ignore <ник>"),
//    commands__ignore__already("&cИгрок &4{username} &cуже был добавлен в игнор."),
    commands__ignore__add("&fИгрок &3{username} &fдобавлен в список игнорируемых"),
    commands__ignore__remove("&fИгрок &3{username} &fубран из списка игнорируемых"),
    commands__ignore__yourself("&cНельзя добавить в игнор самого себя"),

    commands__private_message__usage("&fИспользуйте: &3/{command} <ник> <сообщение>"),
    commands__private_message__ignored("&cВы не можете отправлять сообщение данному игроку"),
    commands__private_message__send("&f[&3Я &f-> &3{username}&f]: {message}"),
    commands__private_message__yourself("&cНельзя отправлять сообщение себе"),
    commands__private_message__player_not_found("&fИгрок &3{username} &fне найден"),
    commands__private_message__receive("&f[&3{username} &f-> &3Я&f]: {message}"),

    commands__private_message__reply_usage("&fИспользуйте: &3/{command} <сообщение>"),
    commands__private_message__reply_none("&fНекому отвечать")


    ;
    List<String> list;
    Langs(String... def){
        list = Arrays.asList(def);
    }

    public List<String> getDefault(){
        return list;
    }

    public String convert(){
        return this.toString().replaceAll("__", "\\.").replaceAll("_", "-");
    }


}
