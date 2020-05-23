package me.enot.wellonline.data.mysql.exception;

public class MySQLDataConnectionFailed extends Exception {

    public MySQLDataConnectionFailed(String message) {
        super(message);
    }

    public MySQLDataConnectionFailed(String comment, String message) {
        this(comment + " " + message);
    }

}
