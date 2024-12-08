package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;
import ru.zenclass.sorokin.bank.services.AccountService;

import java.util.Scanner;

@Component
public class AccountCloseProcessor implements OperationProcessor {
    private final AccountService accountService;
    private final Scanner scanner;

    public AccountCloseProcessor(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account ID to close: ");
        String accountIdStr = scanner.nextLine();
        try {
            long accountId = Long.parseLong(accountIdStr);
            accountService.closeAccount(accountId);
            System.out.println("Account with ID " + accountId + " has been closed.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid account ID!");
        } catch (IllegalArgumentException e) {
            System.err.println("Account with the id does not exist!");
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CLOSE;
    }
}
