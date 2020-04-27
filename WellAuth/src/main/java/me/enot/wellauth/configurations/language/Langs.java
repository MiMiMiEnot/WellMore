package me.enot.wellauth.configurations.language;

import java.util.Arrays;
import java.util.List;

public enum Langs {

    account__account_not_found("&fАккаунт &3{name} &fне найден!"),
    account__account_attempts_history_not_found("&fИстория входов для &3{name} &fне найдена"),
    account__attempts_history_entry( "&3{date}&f &f--> &3{ip}&f &fавторизация: &3{type}"),
    account__attempts_start("&fИстория авторизация для &3{name} &f#&3{limit}:"),
    account__info__player_info_message("&fИнформация на ник &3{name}",
            "&fАйди: &3{id}",
            "&fВремя регистрации (*): &3{reg-time}",
            "&fАйпи регистрации (*): &3{reg-ip}",
            "&fАвторизирован: &3{logged-in}",
            "&fАйпи текущей сессии: &3{session-ip}",
            "&fВремя окончания сессии: &3{session-end-time}",
            "&fАккаунт заблокирован: &3{blocked}",
            "&fЗащита по айпи: &3{whitelist-ip}",
            "&f2FA ключ: &3{2fa-key}",
            "&f2FA включена: &3{2fa-enabled}",
            " ",
            "&3* &f- Время регистрации устанавливается как время первой авторизации " +
                    "в случаи если игрок был зарегистрирован до 27.11.2019, " +
                    "айпи регистрации в таком случаи не известен"),
    command__wellauth__usage("&fИспользуй так: &3/wellauth <history/info> <ник>",
            "&3Дополнительные аргументы:",
            "&3          &l[History]             ",
            "&3[cp\\ct\\wp\\wt] &f - сортировка за результатом авторизации",
            "&f    cp - правильный пароль, ct - правильная 2FA",
            "&f    wp - не правильный пароль, wt - не правильная 2FA",
            "&3[число] - кол-во записей",
            "&3          &l[Info]                ",
            "&fАргументов нет",
            "&3          &l[Dupeip]              ",
            "&fНик \\ Айпи"),
    command__wellauth__dupeip__history_not_found("&fИстория за &3{ARG} &fне найдена :-("),
    command__wellauth__dupeip__history_message("&fИстория для &3{ARG}&f: &f{ARGS}");
    //error__command__wellauth__result_type_not_found("&fРезультат &3{s} &fне обнаружен");

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
