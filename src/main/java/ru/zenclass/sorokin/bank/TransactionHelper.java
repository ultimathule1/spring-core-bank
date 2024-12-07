package ru.zenclass.sorokin.bank;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TransactionHelper {
    SessionFactory sessionFactory;

    public TransactionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T executeInTransaction(Supplier<T> action) {
        Transaction transaction = null;

        try {
            Session session = sessionFactory.getCurrentSession();
            transaction = session.getTransaction();

            if (!transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.begin();
            }

            T returnValue = action.get();

            if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.commit();
            }

            return returnValue;
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.rollback();
            }
            System.out.println("AMA HEREEEEEEEEEEEEEE");
            throw e;
        }
    }
}
