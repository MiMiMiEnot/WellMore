package me.enot.wellchat.configurations.language;

import java.util.Arrays;
import java.util.List;

public enum Langs {

    advertisement__broadcast_to_moderators("&cCC &f{name} &8➯ &f{message}"),
    command__wellauth__usage("&fИспользуй так: &3/wellauth reload&f для перезагрузки конфигурационных файлов"),
    command__wellauth__errors__no_perms("&cУ тебя нет прав для использования этой команды!"),
    command__wellauth__reload_succesful("&aКонфигурационные файлы плагина успешно перезагружено");

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
