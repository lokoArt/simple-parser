package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class BaseDao {
    private Session currentSession;
    private Transaction currentTransaction;
    private SessionFactory sessionFactory;

    protected BaseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session openCurrentSessionwithTransaction() {
        currentSession = sessionFactory.openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        if (currentSession != null) {
            currentSession.close();
            currentTransaction = null;
        }
    }

    public void commitTransaction() {
        currentTransaction.commit();
    }

    protected Session getCurrentSession() {
        return currentSession;
    }
}
