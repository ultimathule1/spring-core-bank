package ru.zenclass.sorokin.bank.configs;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zenclass.sorokin.bank.models.Account;
import ru.zenclass.sorokin.bank.models.User;

@Configuration
public class HibernateConfiguration {
    @Bean
    public SessionFactory sessionFactory() {
        var config = new org.hibernate.cfg.Configuration();

        config
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(User.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .addPackage("ru.zenclass.sorokin.bank")
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "root")
                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update");

        return config.buildSessionFactory();
    }
}
