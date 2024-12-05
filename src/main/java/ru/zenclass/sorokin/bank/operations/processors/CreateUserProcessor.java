package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;
import ru.zenclass.sorokin.bank.services.UserService;

import java.util.Scanner;

@Component
public class CreateUserProcessor implements OperationProcessor {
    private final Scanner scanner;
    private final UserService userService;

    public CreateUserProcessor(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter login for new user:");
        String login = scanner.nextLine();
        var user = userService.createUser(login);
        System.out.println("User created: " + user);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.USER_CREATE;
    }
}
