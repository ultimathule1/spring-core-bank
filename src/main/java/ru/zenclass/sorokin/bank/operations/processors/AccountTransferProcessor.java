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
public class AccountTransferProcessor implements OperationProcessor {
    private final AccountService accountService;
    private final Scanner scanner;

    public AccountTransferProcessor(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter source account ID:");
        try {
            String sourceAccountIdStr = scanner.nextLine();
            long sourceAccountId = Long.parseLong(sourceAccountIdStr);
            System.out.println("Enter target account ID:");
            String targetAccountIdStr = scanner.nextLine();
            long targetAccountId = Long.parseLong(targetAccountIdStr);
            System.out.println("Enter amount to transfer:");
            BigDecimal amountMoney = scanner.nextBigDecimal();
            scanner.nextLine();
            amountMoney = amountMoney.setScale(2, RoundingMode.HALF_UP);
            accountService.transferMoney(sourceAccountId, targetAccountId, amountMoney);
            System.out.printf("Amount %.2f transferred from account ID %d to account ID: %d\n", amountMoney, sourceAccountId, targetAccountId);
        } catch (NumberFormatException | InputMismatchException e) {
            System.err.println("Invalid value!");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_TRANSFER;
    }
}
