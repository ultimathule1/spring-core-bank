package ru.zenclass.sorokin.bank.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import ru.zenclass.sorokin.bank.AccountProperties;
import ru.zenclass.sorokin.bank.TransactionHelper;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountProperties accountProperties;
    private final TransactionHelper transactionHelper;
    private static final String ACCOUNT_NOT_FOUND = "Account not found";
    private static final String AMOUNT_CANNOT_BE_NEGATIVE_OR_ZERO = "Amount can't be negative or zero";
    private final SessionFactory sessionFactory;

    public AccountService(AccountProperties accountProperties,
                          TransactionHelper transactionHelper,
                          SessionFactory sessionFactory){
        this.accountProperties = accountProperties;
        this.transactionHelper = transactionHelper;
        this.sessionFactory = sessionFactory;
    }

    public Account createAccount(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        return transactionHelper.executeInTransaction(() -> {
            Account account = new Account(user, accountProperties.getDefaultAmount());
            sessionFactory.getCurrentSession().persist(account);
            return account;
        });
    }

    public Optional<Account> findAccountById(long id) {
        return Optional.ofNullable(
                transactionHelper.executeInTransaction(() -> {
                    return sessionFactory.getCurrentSession().get(Account.class, id);
                })
        );
    }

    public void depositAccount(long id, BigDecimal amountToDeposit) {
        if (amountToDeposit.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(AMOUNT_CANNOT_BE_NEGATIVE_OR_ZERO);

        transactionHelper.executeInTransaction(() -> {
            Account account = findAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));
            account.setMoneyAmount(account.getMoneyAmount().add(amountToDeposit));
            return 0;
        });
    }

    public void withdrawAccount(long id, BigDecimal amountToWithdraw) {
        if (amountToWithdraw.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(AMOUNT_CANNOT_BE_NEGATIVE_OR_ZERO);

        transactionHelper.executeInTransaction(() -> {
            Account account = findAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));

            if (account.getMoneyAmount().subtract(amountToWithdraw).compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("There are not enough funds for withdrawal." +
                        " The Account cannot be negative.");
            }
            account.setMoneyAmount(account.getMoneyAmount().subtract(amountToWithdraw));
            return 0;
        });
    }

    public Account closeAccount(long id) {
        return transactionHelper.executeInTransaction(() -> {
            Account accountToClose = findAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));

            List<Account> accounts = accountToClose.getUser().getAccounts();
            if (accounts.size() == 1)
                throw new IllegalStateException("You cannot close the account because there is only one account");

            Account firstAccountToDeposit = accounts.stream()
                    .filter(u -> u.getId() != accountToClose.getId())
                    .findFirst()
                    .orElseThrow();

            firstAccountToDeposit.setMoneyAmount(firstAccountToDeposit.getMoneyAmount().add(accountToClose.getMoneyAmount()));
            sessionFactory.getCurrentSession().remove(accountToClose);
            return accountToClose;
        });
    }

    public void transferMoney(long fromId, long toId, BigDecimal amountToTransfer) {
        if (amountToTransfer.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(AMOUNT_CANNOT_BE_NEGATIVE_OR_ZERO);

        transactionHelper.executeInTransaction(() -> {
            Account fromAccount = findAccountById(fromId)
                    .orElseThrow(() -> new IllegalArgumentException("Source Account does not exist"));
            Account toAccount = findAccountById(toId)
                    .orElseThrow(() -> new IllegalArgumentException("Target Account does not exist"));

            BigDecimal totalAmountToTransfer = (fromAccount.getUser().getId()).equals(toAccount.getUser().getId()) ?
                    amountToTransfer :
                    amountToTransfer.add(amountToTransfer
                            .multiply(new BigDecimal(String.valueOf(accountProperties.getTransferCommission()))));

            if (fromAccount.getMoneyAmount().subtract(totalAmountToTransfer).compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("source Account does not have enough money");
            }

            fromAccount.setMoneyAmount(fromAccount.getMoneyAmount().subtract(totalAmountToTransfer));
            toAccount.setMoneyAmount(toAccount.getMoneyAmount().add(amountToTransfer));
            return 0;
        });
    }
}
