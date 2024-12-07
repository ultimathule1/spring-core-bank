package ru.zenclass.sorokin.bank;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.zenclass.sorokin.bank.configs.ProjectConfig;

public class App {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
    }
}
