package mate.academy.spring.dao.impl;

import java.util.List;
import mate.academy.spring.dao.UserDao;
import mate.academy.spring.exception.DataProcessingException;
import mate.academy.spring.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add user: " + user + "to DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    @Override
    public List<User> getAll() {
        String getAllUsers = " FROM User";
        try (Session session = sessionFactory.openSession()) {
            Query<User> getAllUsersQuery = session.createQuery(getAllUsers);
            return getAllUsersQuery.list();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all user from DB", e);
        }
    }
}
