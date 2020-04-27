package me.enot.wellauth.commands;

import me.enot.wellauth.configurations.language.Langs;
import me.enot.wellauth.configurations.language.Replace;
import me.enot.wellauth.mysql.Logick;
import me.enot.wellauth.utils.AuthAccount;
import me.enot.wellauth.utils.Message;
import me.enot.wellauth.utils.Result;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WellAuthCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("wellauth")) {
            if (s.hasPermission("wellauth.cmd.wellauth")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "history":
                        if (args.length > 1) {
                            int limit = 10;
                            Result result = null;
                            try {
                                if (args.length > 2) {
                                    if (isInteger(args[2])) {
                                        limit = Integer.parseInt(args[2]);
                                        if (args.length > 3) {
                                            result = getResultType(args[3]);
                                        }
                                    } else {
                                        result = getResultType(args[2]);
                                        if (args.length > 3) {
                                            limit = Integer.parseInt(args[3]);
                                        }
                                    }
                                }
                                List<String> stringList = Logick.createHistory(args[1], limit, result);
                                if (stringList != null) {
                                    stringList.forEach(s::sendMessage);
                                } else {
                                    Message.getInstance().sendMessage(s, Langs.account__account_attempts_history_not_found,
                                            new Replace("\\{name\\}", args[1]));
                                }
                            } catch (NumberFormatException e) {
                                s.sendMessage(args[2] + " не число");
                            }
                        } else {
                            Message.getInstance().sendMessage(s, Langs.command__wellauth__usage);
                        }
                        break;
                        case "info":
                            if(args.length > 1){
                                String name = args[1];
                                AuthAccount account = Logick.getAccountInfo(name);
                                if(account != null){
                                    SimpleDateFormat f = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
                                    Message.getInstance().sendMessage(s, Langs.account__info__player_info_message,
                                            new Replace("\\{name\\}", name),
                                            new Replace("\\{id\\}", Integer.toString(account.getId())),
                                            new Replace("\\{reg-time\\}", account.getRegTime()),
                                            new Replace("\\{reg-ip\\}", (account.getRegIp() == 0 ? "Отсутствует" : Logick.getIP(account.getRegIp()))),
                                            new Replace("\\{logged-in\\}", account.isLoggedIn() ? "Да" : "Нет"),
                                            new Replace("\\{session-ip\\}", (account.getSessionIp() == 0 ? "Отсутствует" : Logick.getIP(account.getSessionIp()))),
                                            new Replace("\\{session-end-time\\}", (account.getSessionEndTime() == 0 ? "Отсуствует" : f.format(new Date(account.getSessionEndTime())))),
                                            new Replace("\\{blocked\\}", account.isBlocked() ? "Да" : "Нет"),
                                            new Replace("\\{whitelist-ip\\}", account.getWhitelistedIp() == null ? "Отсутствует" : Logick.getIP(account.getWhitelistedIp())),
                                            new Replace("\\{2fa-key\\}", account.getTotpKey() == null ? "Отсутствует" : account.getTotpKey()),
                                            new Replace("\\{2fa-enabled\\}", !account.totpEnabled() ? "Да" : "Нет"));
                                } else {
                                    Message.getInstance().sendMessage(s, Langs.account__account_not_found,
                                            new Replace("\\{name\\}", name));
                                }
                            } else {
                                Message.getInstance().sendMessage(s, Langs.command__wellauth__usage);
                            }
                            break;
                        case "dupeip":
                            if(args.length > 1){
                                String arg = args[1];
                                List<String> list = Logick.dupeipHistory(arg);
                                if(list != null) {
                                    StringBuilder players = new StringBuilder();
                                    for(String string : list){
                                        if(players.toString().equalsIgnoreCase("")){
                                            players.append(string);
                                        } else {
                                            players.append(", ").append(string);
                                        }
                                    }
                                    Message.getInstance().sendMessage(s, Langs.command__wellauth__dupeip__history_message,
                                            new Replace("\\{ARG\\}", arg),
                                            new Replace("\\{ARGS\\}", players.toString()));
                                } else Message.getInstance().sendMessage(s, Langs.command__wellauth__dupeip__history_not_found,
                                        new Replace("\\{ARG\\}", arg));
                            } else Message.getInstance().sendMessage(s, Langs.command__wellauth__usage);
                            break;
                        default:
                            Message.getInstance().sendMessage(s, Langs.command__wellauth__usage);
                    }
                } else {
                    Message.getInstance().sendMessage(s, Langs.command__wellauth__usage);
                }
            }
        }
        return false;
    }

    private boolean isInteger(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private Result getResultType(String s){
        for(Result result : Result.values()){
            if(result.getAliases().contains(s.toLowerCase())){
                return result;
            }
        }
        return null;
    }

    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
