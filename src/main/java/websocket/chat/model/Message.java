package websocket.chat.model;

import java.sql.Time;
import java.util.Date;
import java.sql.Timestamp;

public class Message {

    private String content;
    private String userName;
    private Timestamp dt;

    public Message(String content, String userName) {
        this.content = content;
        this.userName = userName;
        this.dt = new Timestamp(new Date().getTime());
    }

    public Message(String content, String userName, Timestamp dt) {
        this.content = content;
        this.userName = userName;
        this.dt = dt;
    }

    public String getContent() { return this.content; }
    public String getUserName() { return this.userName; }
    public Timestamp getDt() { return this.dt; }
}
