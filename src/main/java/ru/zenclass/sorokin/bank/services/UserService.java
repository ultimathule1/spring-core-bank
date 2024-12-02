package ru.zenclass.sorokin.bank.services;

import org.springframework.stereotype.Service;
import ru.zenclass.sorokin.bank.DAO.DAOUser;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    public final DAOUser daoUser;
    private final AccountService accountService;
    private static long idCounter = 0;

    public UserService(DAOUser daoUser, AccountService accountService) {
        this.daoUser = daoUser;
        this.accountService = accountService;
    }

    public User createUser(String login) {
        if (login == null || login.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        if (daoUser.isLoginTaken(login))
            throw new IllegalArgumentException("Login is already taken");

        User user = new User(++idCounter, login, new ArrayList<>());
        daoUser.saveUser(user);
        accountService.createAccount(user);

        return user;
    }

    public Optional<User> findUserById(long id) {
        return daoUser.findUserById(id);
    }

    public List<User> getAllUsers() {
        return daoUser.getAllUsers();
    }
}