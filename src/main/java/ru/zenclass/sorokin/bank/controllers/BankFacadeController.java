package ru.zenclass.sorokin.bank.controllers;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;
import ru.zenclass.sorokin.bank.services.AccountService;
import ru.zenclass.sorokin.bank.services.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class BankFacadeController {
    private final UserService userService;
    private final AccountService accountService;

    public BankFacadeController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    //---------- User operations-------------//
    public User createUser(String name) {
        return userService.createUser(name);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User findUserById(long id) {
        return userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    //---------- Account operations-------------//

    public Account createAccount(long id) {
        return accountService.createAccount(findUserById(id));
    }

    public Account findAccountById(long id) {
        return accountService.findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public List<Account> getAllUserAccounts(long userId) {
        return accountService.getAllUserAccounts(userId);
    }

    public void depositAccount(long id, BigDecimal amount) {
        accountService.depositAccount(id, amount);
    }

    public void withdrawAccount(long id, BigDecimal amount) {
        accountService.withdrawAccount(id, amount);
    }

    public Account closeAccount(long id) {
        Account account = accountService.closeAccount(id);
        findUserById(account.getUserId()).getAccounts().remove(account);
        return account;
    }

    public void transferMoney(long fromId, long toId, BigDecimal amount) {
        accountService.transferMoney(fromId, toId, amount);
    }
}
