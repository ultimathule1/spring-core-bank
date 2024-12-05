package ru.zenclass.sorokin.bank.services;

import org.springframework.stereotype.Service;
import ru.zenclass.sorokin.bank.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final Map<Long, User> users;
    private final Set<String> takenLogins;
    private final AccountService accountService;
    private long idCounter;


    public UserService(AccountService accountService) {
        this.accountService = accountService;
        idCounter = 0;
        users = new HashMap<>();
        takenLogins = new HashSet<>();
    }

    public User createUser(String login) {
        if (login == null || login.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        if (takenLogins.contains(login))
            throw new IllegalArgumentException("Login is already taken");

        User user = new User(++idCounter, login, new ArrayList<>());
        takenLogins.add(user.getLogin());
        users.put(user.getId(), user);
        accountService.createAccount(user);

        return user;
    }

    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}