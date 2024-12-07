package ru.zenclass.sorokin.bank.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import ru.zenclass.sorokin.bank.TransactionHelper;
import ru.zenclass.sorokin.bank.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final AccountService accountService;
    private final TransactionHelper transactionHelper;
    private final SessionFactory sessionFactory;


    public UserService(AccountService accountService,
                       TransactionHelper transactionHelper, SessionFactory sessionFactory) {
        this.accountService = accountService;
        this.transactionHelper = transactionHelper;
        this.sessionFactory = sessionFactory;
    }

    //Не выходит в консоль, когда создаю юзера, хоть и аккаунт есть. Надо пофиксить
    public User createUser(String login) {
        if (login == null || login.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");

        return transactionHelper.executeInTransaction(() -> {
            Session session = sessionFactory.getCurrentSession();
            User userExist = session
                    .createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResultOrNull();
            if (userExist != null) {
                throw new IllegalArgumentException("User with login " + login + " already exists");
            }
            User user = new User(login, new ArrayList<>());

            session.persist(user);
            accountService.createAccount(user);
            return user;
        });
    }

    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(User.class, id));
    }

    public List<User> getAllUsers() {
        return transactionHelper.executeInTransaction(() -> {
            Session session = sessionFactory.getCurrentSession();
            return session
                    .createQuery("""
                                SELECT s FROM User s
                                LEFT JOIN FETCH s.accounts
                            """, User.class)
                    .list();
        });
    }
}