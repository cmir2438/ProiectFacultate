package concurs.model;

public class User implements HasID<String> {
    private String user;
    private String passw;

    public User(String user, String passw) {
        this.user = user;
        this.passw = passw;
    }
    @Override
    public String getID() {
        return user;
    }
    @Override
    public void setID(String user) {
        this.user = user;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }
}
