package ru.zenclass.sorokin.bank.DAO;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.models.User;
import ru.zenclass.sorokin.bank.services.AccountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class DAOUserImplement implements DAOUser {
    private final Map<Long, User> users;
    private final Set<String> takenLogins;

    public DAOUserImplement() {
        users = new HashMap<>();
        takenLogins = new HashSet<>();
    }

    @Override
    public void saveUser(User user) {
        takenLogins.add(user.getLogin());
        users.put(user.getId() ,user);
    }

    @Override
    public boolean isLoginTaken(String login) {
        return takenLogins.contains(login);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}