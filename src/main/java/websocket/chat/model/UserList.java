package websocket.chat.model;

import java.util.Set;

public class UserList {

    private Set<String> users;

    public UserList(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }
}
