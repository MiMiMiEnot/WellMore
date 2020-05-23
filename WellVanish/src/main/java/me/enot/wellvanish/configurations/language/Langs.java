package me.enot.wellvanish.configurations.language;

import java.util.Arrays;
import java.util.List;

public enum Langs {

    commands__wellvanish__no_permissions("&cУ тебя нет прав на использование данной команды"),
    commands__wellvanish__usage(
            "&fИспользуй так: &3/wellvanish &8(выключает ваниш, если был включен)",
            "&fДоступные аргументы:",
            "&f       &3[history] &f- история ваниша",
            "&f           ->  &3[ник] &f- ник для отображения истории",
            "&f       &3[ник] &f- включение ваниша и телепортация к указаному игроку"
    ),

    commands__wellvanish__history__usage("&fИспользуй так: &3/wellvanish history [ник]"),
    commands__wellvanish__history__entry("&3{name} -> &3{date} &f: &3{online}"),
    commands__wellvanish__history__not_found("&fИстория ваниша игрока &3{name} &fне найдена"),

    commands__wellvanish__vanish__usage("&fИспользуй так: &3/wellvanish [ник]"),
    commands__wellvanish__vanish__enabled("&fВаниш включён, телепортирован к &3{name}"),
    commands__wellvanish__vanish__disabled("&cВаниш выключен.",
            "Телепортирую на местоположение, до ваниша..."),
    commands__wellvanish__vanish__player_offline("&fИгрок &3{name} &fне найден."),

    time_unit__hours("час."),
    time_unit__minute("мин."),
    time_unit__second("сек.");

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
