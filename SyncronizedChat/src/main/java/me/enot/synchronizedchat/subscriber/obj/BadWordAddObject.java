package me.enot.synchronizedchat.subscriber.obj;

public class BadWordAddObject {

    private String type = this.getClass().getSimpleName();

    private String badWord;
    public BadWordAddObject(String badWord) {
        this.badWord = badWord;
    }

    public String getBadWord() {
        return badWord;
    }
}
