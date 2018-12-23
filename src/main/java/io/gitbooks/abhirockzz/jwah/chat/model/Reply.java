package io.gitbooks.abhirockzz.jwah.chat.model;


public class Reply {

    private String message;
    private String from;
    private Boolean isPrivate;

    public Reply(String message, String from, boolean isPrivate) {
        this.message = message;
        this.from = from;
        this.isPrivate = isPrivate;
    }

    public String getMessage() { return this.message; }
    public String getFrom() { return this.from; }
    public boolean getIsPrivate() { return this.isPrivate; }



}
