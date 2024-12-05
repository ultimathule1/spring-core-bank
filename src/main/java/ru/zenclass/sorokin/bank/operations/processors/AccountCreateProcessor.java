package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;
import ru.zenclass.sorokin.bank.services.AccountService;
import ru.zenclass.sorokin.bank.services.UserService;

import java.util.Scanner;

@Component
public class AccountCreateProcessor implements OperationProcessor {
    private final AccountService accountService;
    private final UserService userService;
    private final Scanner scanner;

    public AccountCreateProcessor(AccountService accountService, UserService userService, Scanner scanner) {
        this.accountService = accountService;
        this.userService = userService;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter the user id for which to create an account:");
        String userIdStr = scanner.nextLine();
        try {
            long userId = Long.parseLong(userIdStr);
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with the id does not exist!"));
            Account account = accountService.createAccount(user);
            System.out.printf("New account created with ID: %d, for user: %s\n",
                    account.getId(), user.getLogin());
        } catch (NumberFormatException e) {
            System.err.println("Invalid user id!");
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CREATE;
    }
}
