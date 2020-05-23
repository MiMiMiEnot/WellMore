package me.enot.wellonline.configurations.language;

import java.util.Arrays;
import java.util.List;

public enum Langs {

    error__data__mysql__error_while_connect(
            "&cОшибка подключения к базе данных.",
            "&cОшибка: &4{E}"
    ),

    time_unit__hours("час."),
    time_unit__minute("мин."),
    time_unit__second("сек."),

    commands__wellonline__no_permissions("&cНет прав для исполнения данной команды"),
    commands__wellonline__usage(
            "&fИспользование команды &3/wellonline&f ->",
            "&f/wellonline [ник игрока] <время\\сервер> <сервер\\время>",
            "&3   &l>&r &f[ник игрока] - &3обязательный&f аргумент",
            "&f          ник игрока что будет задействован в команде",
            "&3   &l>&r &f<сервер> - &3не обязательный&f аргумент",
            "&f          сервер, информация с которого будет взята",
            "&f          если не указан - используется текущий сервер",
            "&3   &l>&r &f<дата> - &3не обязательный&f аргумент",
            "&f          дата или промежуток дат, информация за которую будет взята",
            "&f          указывать так: &321.05.2020&f, 01.05.2020-21.05.2020",
            "&f          если не указан - используется сегодняшняя дата",
            "",
            "&3Примеры&f:",
            "&f     /wellonline Player",
            "&f     /wellonline Player 21.05.2020",
            "&f     /wellonline Player duels1 21.05.2020-31.12.2020"
    ),
    commands__wellonline__player_online(
            "&fИгрок &3{player} &fсейчас онлайн",
            "&fВреммя онлайн: &3{time}"
    ),
    commands__wellonline__history("&fИстория за &3{date}&f игрока &3{player} &f-> &3{time}"),
    commands__wellonline__player_history_not_found("&fИстория онлайна игрока &3{name}&f не найдена"),
    commands__wellonline__invalid_date("&fДата &3{date} &fуказана инвалидно");

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
