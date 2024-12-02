package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.controllers.BankFacadeController;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class AccountDepositProcess implements OperationProcessor {
    private final BankFacadeController controller;
    private final Scanner scanner;

    public AccountDepositProcess(BankFacadeController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account ID:");
        try {
            String accountIdStr = scanner.nextLine();
            long accountId = Long.parseLong(accountIdStr);
            System.out.println("Enter amount to deposit:");
            BigDecimal amountMoney = scanner.nextBigDecimal();
            scanner.nextLine();
            amountMoney = amountMoney.setScale(2, RoundingMode.HALF_UP);
            controller.depositAccount(accountId, amountMoney);
            System.out.printf("Amount %.2f deposited to account ID: %d\n", amountMoney, accountId);
        } catch (NumberFormatException | InputMismatchException e) {
            System.err.println("Invalid value!");
        } catch (IllegalArgumentException e) {
            System.err.println("Account with the id does not exist!");
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_DEPOSIT;
    }
}
