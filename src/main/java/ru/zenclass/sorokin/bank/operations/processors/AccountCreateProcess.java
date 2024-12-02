package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.controllers.BankFacadeController;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;

import java.util.Scanner;

@Component
public class AccountCreateProcess implements OperationProcessor {
    private final BankFacadeController controller;
    private final Scanner scanner;

    public AccountCreateProcess(BankFacadeController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter the user id for which to create an account:");
        String userIdStr = scanner.nextLine();
        try {
            long userId = Long.parseLong(userIdStr);
            Account account = controller.createAccount(userId);
            System.out.printf("New account created with ID: %d, for user: %s\n",
                    account.getId(), controller.findUserById(userId).getLogin());
        } catch (NumberFormatException e) {
            System.err.println("Invalid user id!");
        } catch (IllegalArgumentException e) {
            System.err.println("User with the id does not exist!");
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CREATE;
    }
}
