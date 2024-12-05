package ru.zenclass.sorokin.bank;

import org.springframework.stereotype.Component;
import ru.zenclass.sorokin.bank.operations.OperationProcessor;
import ru.zenclass.sorokin.bank.operations.OperationType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class OperationsConsoleListener {
    private final Scanner scanner;
    private final Map<OperationType, OperationProcessor> processors;
    private final String EXIT = "EXIT";

    public OperationsConsoleListener(Scanner scanner,
                                     List<OperationProcessor> processors) {
        this.scanner = scanner;
        this.processors = processors
                .stream()
                .collect(
                        Collectors.toMap(
                                OperationProcessor::getOperationType,
                                processor -> processor
                        )
                );
    }

    public void start() {
        System.out.println("The Bank App started!");
        startListen();
    }

    private void startListen() {
        listenUpdates();
    }

    private void listenUpdates() {
        Optional<OperationType> operationType;

        while (isListening()) {
            printOperations();
            operationType = listenNextOperation();
            if (Thread.interrupted()) {
                break;
            }
            if (operationType.isEmpty()) {
                continue;
            }
            processOperation(operationType.get());
        }
    }

    public boolean isListening() {
        return !Thread.currentThread().isInterrupted();
    }

    public void stopListen() {
        System.out.println("The App stopped!");
        Thread.currentThread().interrupt();
    }

    private Optional<OperationType> listenNextOperation() {
        String str = scanner.nextLine();
        OperationType ot = null;

        if (str.equals(EXIT)) {
            stopListen();
            return Optional.empty();
        }

        try {
            ot = OperationType.valueOf(str);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid operation type: " + str);
        }
        return Optional.ofNullable(ot);
    }

    private void processOperation(OperationType operationType) {
        OperationProcessor operationProcessor = processors.get(operationType);
        try {
            operationProcessor.processOperation();
        } catch (Exception e) {
            System.err.println("Error processing operation: " + e.getMessage());
        }
    }

    private void printOperations() {
        System.out.println("Please enter one of operation types: ");
        processors.keySet().forEach(operationType -> {
            System.out.println("-" + operationType);
        });
        System.out.println("-" + EXIT);
    }
}
