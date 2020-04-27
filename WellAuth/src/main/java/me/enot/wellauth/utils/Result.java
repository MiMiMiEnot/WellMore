package me.enot.wellauth.utils;

import java.util.Arrays;
import java.util.List;

public enum Result {

    CORRECT_PASSWORD("cp", "correct_password", "correct_p", "c_password", "correctpassword", "corp"),
    WRONG_PASSWORD("wp", "wrong_password", "wrong_p", "w_password", "wrongpassword", "wrp"),
    CORRECT_TOTP_CODE("ctc", "correct_totp_code", "correct_t_c", "correct_tc", "c_totp_code", "c_t_code", "cort"),
    WRONG_TOTP_CODE("wtc", "wrong_totp_code", "wront_t_c", "wrong_tc", "w_totp_code", "w_t_code", "wrt");

    private List<String> aliases;

    Result(String... strings){
        this.aliases = Arrays.asList(strings);
    }

    public List<String> getAliases() {
        return aliases;
    }
}
