package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.controllers.BankFacadeController;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;

import java.util.Scanner;

@Component
public class CreateUserProcess implements OperationProcessor {
    private final Scanner scanner;
    private final BankFacadeController controller;

    public CreateUserProcess(Scanner scanner, BankFacadeController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter login for new user:");
        String login = scanner.nextLine();
        var user = controller.createUser(login);
        System.out.println("User created: " + user);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.USER_CREATE;
    }
}
