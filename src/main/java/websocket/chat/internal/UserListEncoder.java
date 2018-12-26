package websocket.chat.internal;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import websocket.chat.model.UserList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserListEncoder implements Encoder.Text<UserList> {

    @Override
    public String encode(UserList userList) throws EncodeException {
        JSONObject obj = new JSONObject();
        obj.put("type", "users");

        JSONArray users = new JSONArray();
        for (String user : userList.getUsers()) {
            users.add(user);
        }
        obj.put("users", users);
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

}
