package ru.zenclass.sorokin.bank.DAO;

import ru.zenclass.sorokin.bank.models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DAOAccount {
    void saveAccount(Account account);
    Optional<Account> findAccountById(long id);
    List<Account> getUserAccounts(long userId);
    void depositAccount(long id, BigDecimal amount);
    void withdrawAccount(long id, BigDecimal amountToWithdraw);
    Account closeAccount(Account accountToClose);
    void transfer(Account fromAccount, Account toAccount, BigDecimal amountToTransfer, double transferCommission);
}

