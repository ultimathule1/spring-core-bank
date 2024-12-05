package ru.zenclass.sorokin.bank.models;

import java.util.List;

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
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accounts=" + accounts.toString() +
                '}';
    }
}
