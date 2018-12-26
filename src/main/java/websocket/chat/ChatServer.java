package websocket.chat;

import websocket.chat.connection.MessagesDao;
import websocket.chat.internal.MessageEncoder;
import websocket.chat.internal.UserListEncoder;
import websocket.chat.model.Message;
import websocket.chat.model.UserList;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(
        value = "/chat/{user}/",
        encoders = {MessageEncoder.class,
                    UserListEncoder.class}
)

public class ChatServer {

    private static final Set<String> USERS = new ConcurrentSkipListSet<>();

    private String user;
    private Session s;

    @OnOpen
    public void userConnectedCallback(@PathParam("user") String user, Session s) {
        this.s = s;
        s.getUserProperties().put("user", user);
        this.user = user;
        USERS.add(user);
        sendUsers();
        sendPreviousMessages(300);
    }

    private void sendPreviousMessages(int maxNb){
        List<Message> messages = MessagesDao.getMessages(maxNb);
        for (Message message: messages){
            s.getAsyncRemote().sendObject(message);
        }
    }

    private void sendUsers() {
        s.getOpenSessions().stream()
                .filter((sn) -> sn.isOpen())
                .forEach((session) -> session.getAsyncRemote().sendObject(new UserList(USERS)));
    }

    @OnMessage
    public void msgReceived(String content, Session s) {
        Message message = new Message(content, user);
        // Inserting message in DB
        MessagesDao.addMessage(message);
        s.getOpenSessions().stream()
                .filter((sn) -> sn.isOpen())
                .forEach((session) -> session.getAsyncRemote().sendObject(message));
    }

    @OnClose
    public void onCloseCallback() {
            processLogout();
    }

    private void processLogout() {
        try {
            USERS.remove(this.user);
            sendUsers();
        } catch (Exception ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
