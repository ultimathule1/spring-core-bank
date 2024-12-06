package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;
import ru.zenclass.sorokin.bank.services.AccountService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class AccountWithdrawProcessor implements OperationProcessor {
    private final AccountService accountService;
    private final Scanner scanner;

    public AccountWithdrawProcessor(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account ID to withdraw from:");
        try {
            String accountIdStr = scanner.nextLine();
            long accountId = Long.parseLong(accountIdStr);
            System.out.println("Enter amount to withdraw:");
            BigDecimal amountMoney = scanner.nextBigDecimal();
            scanner.nextLine();
            amountMoney = amountMoney.setScale(2, RoundingMode.HALF_UP);
            accountService.withdrawAccount(accountId, amountMoney);
            System.out.printf("Amount %.2f withdrew to account ID: %d\n", amountMoney, accountId);
        } catch (NumberFormatException | InputMismatchException e) {
            System.err.println("Invalid value!");
        } catch (IllegalArgumentException e) {
            System.err.println("Error executing command ACCOUNT_WITHDRAW: error=No such money to withdraw");
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_WITHDRAW;
    }
}
