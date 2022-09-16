package amacic.services.UserService;

import amacic.data.User;

import java.util.List;

public interface UserService {
    public User addUser(User user);

    public User findUserByEmail(String email);

    public List<User> listAllUsers(int offset, int limit);

    public User editUser(User user);

    public String login(String username, String password);
}