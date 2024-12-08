package ru.zenclass.sorokin.bank;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TransactionHelper {
    SessionFactory sessionFactory;

    public TransactionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T executeInTransaction(Supplier<T> action) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();

        if (transaction != null && transaction.isActive()) {
            return action.get();
        }

        try {
            transaction = session.beginTransaction();
            T returnValue = action.get();
            transaction.commit();
            return returnValue;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
