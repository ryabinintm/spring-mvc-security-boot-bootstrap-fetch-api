package web.service;

import web.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByEmail(String email);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

}
