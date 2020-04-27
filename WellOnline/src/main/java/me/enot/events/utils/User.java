package me.enot.events.utils;

public class User {

    private String username;
    private long time;
    private long date;
    private String server;

    public User(String username, long time, long date, String server){
        this.username = username;
        this.time = time;
        this.date = date;
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public long getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public String getServer() {
        return server;
    }
}
