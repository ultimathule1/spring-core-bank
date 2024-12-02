package ru.zenclass.sorokin.bank.models;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User {
    private final long id;
    private final String login;
    private final List<Account> accounts;

    public User(long id, String name, List<Account> accounts) {
        this.id = id;
        this.login = name;
        this.accounts = accounts;
    }

    public User(User user) {
        this.id = user.id;
        this.login = user.login;
        accounts = user.getAccounts();
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accounts=" + accounts.toString() +
                '}';
    }
}
