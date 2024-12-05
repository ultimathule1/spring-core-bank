package ru.zenclass.sorokin.bank.services;

import org.springframework.stereotype.Service;
import ru.zenclass.sorokin.bank.AccountProperties;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {
    private final Map<Long, Account> userAccounts;
    private final AccountProperties accountProperties;
    private long idCounter;
    private final String ACCOUNT_NOT_FOUND = "Account not found";

    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
        idCounter = 0;
        userAccounts = new HashMap<>();
    }

    public Account createAccount(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        Account account = new Account(++idCounter, user.getId(), accountProperties.getDefaultAmount());
        user.getAccounts().add(account);
        userAccounts.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findAccountById(long id) {
        return Optional.ofNullable(userAccounts.get(id));
    }

    public List<Account> getAllUserAccounts(long userId) {
        if (userId < 0)
            throw new IllegalArgumentException("User id cannot be negative");

        return userAccounts.values().stream()
                .filter(a -> a.getUserId() == userId)
                .toList();
    }

    public void depositAccount(long id, BigDecimal amountToDeposit) {
        if (amountToDeposit.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");
        if (findAccountById(id).isEmpty())
            throw new IllegalArgumentException(ACCOUNT_NOT_FOUND);

        Account account = findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));
        account.setMoneyAmount(
                account.getMoneyAmount().add(amountToDeposit)
        );
    }

    public void withdrawAccount(long id, BigDecimal amountToWithdraw) {
        Account account = findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));
        if (amountToWithdraw.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");

        if (account.getMoneyAmount().subtract(amountToWithdraw).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("There are not enough funds for withdrawal." +
                    " The Account cannot be negative.");
        }

        account.setMoneyAmount(
                account.getMoneyAmount().subtract(amountToWithdraw)
        );
    }

    public Account closeAccount(long id) {
        Account accountToClose = findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));

        List<Account> accounts = getAllUserAccounts(accountToClose.getUserId());
        if (accounts.size() == 1)
            throw new IllegalStateException("You cannot close the account because there is only one account");

        Account firstAccountToDeposit = accounts.stream()
                .filter(u -> u.getId() != accountToClose.getId())
                .findFirst()
                .orElseThrow();

        firstAccountToDeposit
                .setMoneyAmount(accountToClose.getMoneyAmount()
                        .add(firstAccountToDeposit.getMoneyAmount()));

        userAccounts.remove(accountToClose.getId());

        return accountToClose;
    }

    public void transferMoney(long fromId, long toId, BigDecimal amountToTransfer) {
        Account fromAccount = findAccountById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Source Account does not exist"));
        Account toAccount = findAccountById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Target Account does not exist"));
        if (amountToTransfer.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount cannot be negative or zero");


        BigDecimal totalAmountToTransfer = fromAccount.getUserId() == toAccount.getUserId() ?
                amountToTransfer :
                amountToTransfer.add(amountToTransfer
                        .multiply(new BigDecimal(String.valueOf(accountProperties.getTransferCommission()))));

        if (fromAccount.getMoneyAmount().subtract(totalAmountToTransfer).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("source Account does not have enough money");
        }

        fromAccount.setMoneyAmount(fromAccount.getMoneyAmount().subtract(totalAmountToTransfer));
        toAccount.setMoneyAmount(toAccount.getMoneyAmount().add(amountToTransfer));
    }
}
