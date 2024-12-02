package ru.zenclass.sorokin.bank.services;

import org.springframework.stereotype.Service;
import ru.zenclass.sorokin.bank.AccountProperties;
import ru.zenclass.sorokin.bank.DAO.DAOAccountImplement;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final DAOAccountImplement daoAccount;
    private final AccountProperties accountProperties;
    private static long idCounter = 0;

    public AccountService(DAOAccountImplement daoAccount, AccountProperties accountProperties) {
        this.daoAccount = daoAccount;
        this.accountProperties = accountProperties;
    }

    public Account createAccount(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        Account account = new Account(++idCounter, user.getId(), accountProperties.getDefaultAmount());
        user.getAccounts().add(account);
        daoAccount.saveAccount(account);
        return account;
    }

    public Optional<Account> findAccountById(long id) {
        return daoAccount.findAccountById(id);
    }

    public List<Account> getAllUserAccounts(long userId) {
        if (userId < 0)
            throw new IllegalArgumentException("User id cannot be negative");

        return daoAccount.getUserAccounts(userId);
    }

    public void depositAccount(long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");
        if (findAccountById(id).isEmpty())
            throw new IllegalArgumentException("Account does not exist");

        daoAccount.depositAccount(id, amount);
    }

    public void withdrawAccount(long id, BigDecimal amount) {
        Account account = findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account does not exist"));
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");

        if (account.getMoneyAmount().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("There are not enough funds for withdrawal." +
                    " The Account cannot be negative.");
        }

        daoAccount.withdrawAccount(id, amount);
    }

    public Account closeAccount(long id) {
        Account accountToClose = findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account does not exist"));
        return daoAccount.closeAccount(accountToClose);
    }

    public void transferMoney(long fromId, long toId, BigDecimal amount) {
        Account fromAccount = findAccountById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Source Account does not exist"));
        Account toAccount = findAccountById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Target account does not exist"));
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");
        if (fromAccount.getMoneyAmount().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("source Account does not have enough money");
        }

        daoAccount.transfer(fromAccount, toAccount, amount, accountProperties.getTransferCommission());
    }
}
