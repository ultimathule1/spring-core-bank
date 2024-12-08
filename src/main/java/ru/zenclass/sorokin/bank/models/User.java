package ru.zenclass.sorokin.bank.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    //Возможно здесь нужен FetchType.EAGER-------------------------------------------
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Account> accounts;

    public User() {
    }

    public User(String name, List<Account> accounts) {
        this.login = name;
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
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
