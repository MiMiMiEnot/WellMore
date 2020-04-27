package me.enot.wellauth.utils;

public class AuthAccount {

    private int id;
    private String regTime;
    private long regIp;
    private boolean loggedIn;
    private Long sessionIp;
    private Long sessionEndTime;
    private boolean blocked;
    private Long whitelistedIp;
    private String totpKey;
    private boolean totpEnabled;

    public AuthAccount(int id, String regTime, long regIp, boolean loggedIn, Long sessionIp, Long sessionEndTime,
                       boolean blocked, Long whitelistedIp, String totpKey, boolean totpEnabled){
        this.id = id;
        this.regTime = regTime;
        this.regIp = regIp;
        this.loggedIn = loggedIn;
        this.sessionIp = sessionIp;
        this.sessionEndTime = sessionEndTime;
        this.blocked = blocked;
        this.whitelistedIp = whitelistedIp;
        this.totpKey = totpKey;
        this.totpEnabled = totpEnabled;
    }

    public int getId() {
        return id;
    }

    public String getRegTime() {
        return regTime;
    }

    public long getRegIp() {
        return regIp;
    }

    public Long getSessionEndTime() {
        return sessionEndTime;
    }

    public Long getSessionIp() {
        return sessionIp;
    }

    public Long getWhitelistedIp() {
        return whitelistedIp;
    }

    public String getTotpKey() {
        return totpKey;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean totpEnabled() {
        return totpEnabled;
    }
}
