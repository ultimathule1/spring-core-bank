package ru.zenclass.sorokin.bank.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = {"ru.zenclass.sorokin.bank.services", "ru.zenclass.sorokin.bank.models",
        "ru.zenclass.sorokin.bank"})
@PropertySource("classpath:application.properties")
public class ProjectConfig {
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}
