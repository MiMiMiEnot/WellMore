package me.enot.wellgui.configurations.language;

import java.util.Arrays;
import java.util.List;

public enum Langs {

    main__load__load_guis("&fПрогружено &3{X} &fменю за &3{Y} &fмс"),
    main__load__load_error_enchant("&cОшибка при прогрузке заклинания &4{E} &c{EXCEPTION-MESSAGE}"),
    gui__clock__to_quick("&cНе кликайте так часто!"),
    gui__click__gio__gui_not_found_by_id("&cОшибка!",
            "&fПожалуйста, обратись к администрации для решения проблемы",
            "&fДополнительная информация для администрации: &c{MESSAGE}"),
    gui__click__gis__server_full("&cСервер заполненный",
            "&fДля захода на заполненный сервер нужно иметь привилегию &cТень&r или выше"),
    gui__click__gis__require_vk_link("&cДля захода на этот сервер нужно, чтобы твой аккаунт был привязан к ВК-боту."),
    gui__click__gis__require_vk_error("&c&lСлучилась ошибка при исполнении запроса на отправку вас на сервер. Пожалуйста, сообщите код администрации: {code}"),
    gui__open__please_wait("&cПожалуйста, подождите несколько секунд, прежде чем пользоваться меню."),
    commands__wellgui__help("&fИспользуй так: &3/wellgui reload [configs/guis/all]"),
    commands__wellgui__reload_configs("&aКонфигурационные файлы перезагружены"),
    commands__wellgui__reload_guis("&aГУИ файлы перезагруженны"),
    commands__wellgui__reload_all("&aКонфигурационные файлы и ГУИ файлы перезагружены"),
    commands__wellgui__reload_argument_not_found("&fАргумент &3{ARG} &fне корректный."),
    commands__no_permissions("&cНет прав!"),
    commands__guisend__send("&fИгрок {name} отправлен на сервер {server}, привязка {link}")
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
