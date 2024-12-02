package ru.zenclass.sorokin.bank;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ConsoleListenerStarter {
    private final OperationsConsoleListener listener;

    public ConsoleListenerStarter(OperationsConsoleListener listener) {
        System.out.println("Welcome To The Bank App!");
        this.listener = listener;
    }

    @PostConstruct
    public void postConstruct() {
        Thread consoleThread = new Thread(listener::start);
        consoleThread.start();
    }
}
