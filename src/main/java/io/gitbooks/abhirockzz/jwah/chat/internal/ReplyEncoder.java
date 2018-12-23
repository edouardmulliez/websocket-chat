package io.gitbooks.abhirockzz.jwah.chat.internal;

import io.gitbooks.abhirockzz.jwah.chat.model.Reply;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.json.simple.JSONObject;

public class ReplyEncoder implements Encoder.Text<Reply> {

    @Override
    public String encode(Reply reply) throws EncodeException {
        //System.out.println("reply.getMsg() "+reply.getMsg());

        JSONObject obj = new JSONObject();
        obj.put("type", "reply");
        obj.put("from", reply.getFrom());
        obj.put("message", reply.getMessage());
        obj.put("isPrivate", reply.getIsPrivate());
        return obj.toJSONString();

//        return reply.getMsg();
    }

    @Override
    public void init(EndpointConfig ec) {
        //no-op
    }

    @Override
    public void destroy() {
        //no-op
    }
    
}
