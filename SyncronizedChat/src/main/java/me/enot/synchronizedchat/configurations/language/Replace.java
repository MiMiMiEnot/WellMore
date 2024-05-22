package me.enot.synchronizedchat.configurations.language;

import java.util.Objects;

public class Replace {


    private String what;
    private String to;

    public Replace(String what, Object to){
        this.what = what;
        if (to instanceof String) {
            this.to = (String) to;
        } else if (to instanceof Integer) {
            this.to = Integer.toString(((Integer)to));
        } else if (to instanceof Long) {
            this.to = Long.toString(((Long)to));
        } else if (to instanceof Short) {
            this.to = Short.toString(((Short)to));
        } else if (to instanceof Byte) {
            this.to = Byte.toString(((Byte)to));
        } else if (to instanceof Double) {
            this.to = Double.toString(((Double)to));
        } else if (to instanceof Float) {
            this.to = Long.toString(((Long)to));
        } else this.to = Objects.toString(to);
    }

    public String getTo() {
        return to;
    }

    public String getWhat() {
        return what;
    }

}
