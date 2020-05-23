package me.enot.wellonline.data;

import me.enot.wellonline.configurations.language.Langs;
import me.enot.wellonline.utils.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private Utils() {}

    public static String getTimeByLong(long time) {
        StringBuilder builder = new StringBuilder();
        time /= 1000; // В секунды
        final int hour = 3600;
        final int minute = 60;

        int hours = (int) (time > hour ? time / hour : 0);
        time -= hours * hour;
        int minutes = (int) (time > minute ? time / minute : 0);
        time -= minutes * minute;
        int seconds = (int) time;

        String h = (String) Message.getInstance().getObjectMessage(Langs.time_unit__hours);
        String m = (String) Message.getInstance().getObjectMessage(Langs.time_unit__minute);
        String s = (String) Message.getInstance().getObjectMessage(Langs.time_unit__second);

        if (hours > 0) builder.append(hours).append(h);
        if (hours > 0) builder.append(minutes).append(m);
        if (hours > 0) builder.append(seconds).append(s);

        return builder.toString();
    }

    public static Long convertToDayStart(long date) {
        return convertToDay(date, "00:00:00");
    }

    public static Long convertToDayEnd(long date) {
        return convertToDay(date, "23:59:59");
    }

    public static Long convertToDay(long date, String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date d = new Date(date);
        String f = format.format(d) + " " + time;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            Date dd = format1.parse(f);
            return dd.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
