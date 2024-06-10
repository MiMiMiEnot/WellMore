package me.enot.wellchat.configurations.language;

import java.util.Arrays;
import java.util.List;

public enum Langs {

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
