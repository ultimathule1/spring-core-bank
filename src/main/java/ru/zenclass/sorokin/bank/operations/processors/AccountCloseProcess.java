package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.controllers.BankFacadeController;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;

import java.util.Scanner;

@Component
public class AccountCloseProcess implements OperationProcessor {
    private final BankFacadeController controller;
    private final Scanner scanner;

    public AccountCloseProcess(BankFacadeController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account ID to close: ");
        String accountIdStr = scanner.nextLine();
        try {
            long accountId = Long.parseLong(accountIdStr);
            controller.closeAccount(accountId);
            System.out.println("Account with ID " + accountId + " has been closed.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid account ID!");
        } catch (IllegalArgumentException e) {
            System.err.println("Account with the id does not exist!");
        } catch (IllegalStateException e) {
            System.err.println("The account cannot be closed since it is the only one the user has");
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CLOSE;
    }
}
