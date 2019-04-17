package concurs.persistance;

import concurs.model.User;

public interface IRepositoryUsers {
    public User findBy(String user);
    public User findBy(String user, String passwd);
}
