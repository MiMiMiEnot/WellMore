package me.enot.wellgui.configurations.language;

public class Replace {

    private String what;
    private String to;

    public Replace(String what, String to){
        this.what = what;
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public String getWhat() {
        return what;
    }

}
