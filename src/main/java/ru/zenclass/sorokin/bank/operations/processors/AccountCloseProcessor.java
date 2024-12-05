package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;
import ru.zenclass.sorokin.bank.services.AccountService;
import ru.zenclass.sorokin.bank.services.UserService;

import java.util.Scanner;

@Component
public class AccountCloseProcessor implements OperationProcessor {
    private final AccountService accountService;
    private final UserService userService;
    private final Scanner scanner;

    public AccountCloseProcessor(AccountService accountService, UserService userService, Scanner scanner) {
        this.accountService = accountService;
        this.userService = userService;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account ID to close: ");
        String accountIdStr = scanner.nextLine();
        try {
            long accountId = Long.parseLong(accountIdStr);
            Account account = accountService.closeAccount(accountId);
            userService.findUserById(account.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found!"))
                    .getAccounts().remove(account);
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
