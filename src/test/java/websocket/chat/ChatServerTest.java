package websocket.chat;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ChatServerTest {

    public ChatServerTest() {
    }

    private WebSocketServerManager instance;
    private CountDownLatch controlLatch;
    private static final String BASE_SERVER_URL = "ws://localhost:8080/chat/";

    @Before
    public void setUp() {
        try {
            instance = new WebSocketServerManager();
            instance.runServer(false);
        } catch (Exception ex) {
            Logger.getLogger(ChatServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @After
    public void tearDown() {
        instance.stop();
        controlLatch = null;
    }

    class ChatClient extends Endpoint {

        private List<String> responses = new ArrayList<>();
        private Session sn;

        @Override
        public void onOpen(Session sn, EndpointConfig ec) {
            this.sn = sn;
            controlLatch.countDown();
            sn.addMessageHandler(String.class, s -> {
                responses.add(s);
                controlLatch.countDown();
            });
        }

        public boolean isMessageReceived(String message) {
            return responses.contains(message);
        }

        public List<String> getMessages() {
            return responses;
        }

        public void sendMessage(String msg) {
            try {
                sn.getBasicRemote().sendText(msg);
            } catch (IOException ex) {
                Logger.getLogger(ChatServerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void closeConnection(){
            try {
                sn.close();
            } catch (IOException ex) {
                Logger.getLogger(ChatServerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private CloseReason closeReason;
        
        @Override
        public void onClose(Session session, CloseReason closeReason) {
            this.closeReason = closeReason;
            if(connClosedLatch!=null){
                connClosedLatch.countDown();
            }
            
        }
        
        public CloseReason getCloseReason(){
            return this.closeReason;
        }
        
    }
    
    private CountDownLatch connClosedLatch = null;

    @Test
    public void sendPublicMsgToAll() throws Exception {
        controlLatch = new CountDownLatch(2);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        ChatClient abhi = new ChatClient();
        container.connectToServer(abhi,
                ClientEndpointConfig.Builder.create().build(),
                URI.create(BASE_SERVER_URL + "abhi/"));

        assertTrue(controlLatch.await(5, TimeUnit.SECONDS));

        controlLatch = new CountDownLatch(3);

        ChatClient gitu = new ChatClient();
        container.connectToServer(gitu,
                ClientEndpointConfig.Builder.create().build(),
                URI.create(BASE_SERVER_URL + "gitu/"));

        assertTrue(controlLatch.await(5, TimeUnit.SECONDS));

        controlLatch = new CountDownLatch(1);

        String publicMsg = "hey everyone!!";
        gitu.sendMessage(publicMsg); //send public message to ALL
        assertTrue(controlLatch.await(5, TimeUnit.SECONDS)); // wait for message to be received
        TimeUnit.SECONDS.sleep(1);

        assertTrue(abhi.getMessages().stream().anyMatch(msg -> msg.contains(publicMsg)));

        abhi.closeConnection();
        gitu.closeConnection();
    }

}
