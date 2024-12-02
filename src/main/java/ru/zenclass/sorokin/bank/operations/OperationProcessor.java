package ru.zenclass.sorokin.bank.operations;

public interface OperationProcessor {
    void processOperation();
    OperationType getOperationType();
}
