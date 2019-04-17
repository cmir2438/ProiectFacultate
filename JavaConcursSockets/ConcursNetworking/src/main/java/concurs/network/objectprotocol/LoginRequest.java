package concurs.network.objectprotocol;

import concurs.model.User;

public class LoginRequest implements Request {
    private User user;

    public LoginRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
