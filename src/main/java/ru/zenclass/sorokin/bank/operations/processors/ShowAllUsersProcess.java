package ru.zenclass.sorokin.bank.operations.processors;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.controllers.BankFacadeController;
import ru.zenclass.sorokin.bank.models.User;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;

import java.util.List;

@Component
public class ShowAllUsersProcess implements OperationProcessor {
    private final BankFacadeController controller;
    public ShowAllUsersProcess(BankFacadeController controller) {
        this.controller = controller;
    }

    @Override
    public void processOperation() {
        List<User> userList = controller.getAllUsers();
        System.out.println("List of all users:");
        userList.forEach(System.out::println);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.SHOW_ALL_USERS;
    }
}
