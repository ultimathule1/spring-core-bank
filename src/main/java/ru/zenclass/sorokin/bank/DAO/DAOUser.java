package ru.zenclass.sorokin.bank.DAO;

import ru.zenclass.sorokin.bank.models.User;

import java.util.List;
import java.util.Optional;

public interface DAOUser {
    void saveUser(User user);
    Optional<User> findUserById(long id);
    boolean isLoginTaken(String login);
    List<User> getAllUsers();
}
