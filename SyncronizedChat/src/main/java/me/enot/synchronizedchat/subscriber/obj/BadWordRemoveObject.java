package me.enot.synchronizedchat.subscriber.obj;

public class BadWordRemoveObject {

    private String type = this.getClass().getSimpleName();

    private String badWord;
    public BadWordRemoveObject(String badWord) {
        this.badWord = badWord;
    }

    public String getBadWord() {
        return badWord;
    }

}
