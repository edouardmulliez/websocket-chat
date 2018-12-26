package io.gitbooks.abhirockzz.jwah.chat.internal;

import io.gitbooks.abhirockzz.jwah.chat.model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public String encode(Message message) throws EncodeException {
        JSONObject obj = new JSONObject();
        obj.put("type", "message");
        obj.put("userName", message.getUserName());
        obj.put("content", message.getContent());
        obj.put("dt", encodeTimestamp(message.getDt()));
        return obj.toJSONString();
    }

    @Override
    public void init(EndpointConfig ec) {
        //no-op
    }

    @Override
    public void destroy() {
        //no-op
    }

    private static String encodeTimestamp(Timestamp dt){
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(dt);
    }
}
