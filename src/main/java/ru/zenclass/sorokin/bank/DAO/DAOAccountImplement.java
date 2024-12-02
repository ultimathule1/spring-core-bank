package ru.zenclass.sorokin.bank.DAO;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DAOAccountImplement implements DAOAccount{
    private final Map<Long, Account> userAccounts;

    public DAOAccountImplement() {
        userAccounts = new HashMap<>();
    }

    @Override
    public void saveAccount(Account account) {
        userAccounts.put(account.getId(), account);
    }

    @Override
    public Optional<Account> findAccountById(long id) {
        return Optional.ofNullable(userAccounts.get(id));
    }

    @Override
    public List<Account> getUserAccounts(long userId) {
        return userAccounts.values().stream()
                .filter(a -> a.getUserId() == userId)
                .toList();
    }

    @Override
    public void depositAccount(long id, BigDecimal amountToDeposit) {
        Account account = findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setMoneyAmount(
                account.getMoneyAmount().add(amountToDeposit)
        );
    }

    @Override
    public void withdrawAccount(long id, BigDecimal amountToWithdraw) {
        Account account = findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setMoneyAmount(
                account.getMoneyAmount().subtract(amountToWithdraw)
        );
    }

    @Override
    public Account closeAccount(Account accountToClose) {
        if (accountToClose == null)
            throw new IllegalArgumentException("Account not found");

        List<Account> accounts = getUserAccounts(accountToClose.getUserId());
        if (accounts.size() == 1)
            throw new IllegalStateException("You cannot close the account because there is only one account");

        Account firstAccountToDeposit = accounts.stream()
                .filter(u -> u.getId() == accountToClose.getId())
                .findFirst()
                .orElseThrow();

        firstAccountToDeposit
                .setMoneyAmount(accountToClose.getMoneyAmount()
                        .add(firstAccountToDeposit.getMoneyAmount()));

        userAccounts.remove(accountToClose.getId());

        return accountToClose;
    }

    public void transfer(Account fromAccount, Account toAccount, BigDecimal amountToTransfer, double transferCommission) {
        if (fromAccount == null || toAccount == null || amountToTransfer == null) {
            throw new IllegalArgumentException("One of the arguments in the transfer is null");
        }

        BigDecimal totalAmountToTransfer = fromAccount.getUserId() == toAccount.getUserId() ?
                amountToTransfer :
                amountToTransfer.add(amountToTransfer.multiply(new BigDecimal(String.valueOf(transferCommission))));

        fromAccount.setMoneyAmount(fromAccount.getMoneyAmount().subtract(totalAmountToTransfer));
        toAccount.setMoneyAmount(toAccount.getMoneyAmount().add(totalAmountToTransfer));
    }
}