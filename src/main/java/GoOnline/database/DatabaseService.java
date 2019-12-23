package GoOnline.database;

import GoOnline.domain.Game.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public class DatabaseService {
    private DatabaseService databaseService;
    @Autowired
    private SessionFactory sessionFactory;

    private DatabaseService()
    {

    }
    public DatabaseService getInstance()
    {
        if(databaseService == null)
        {
            databaseService = new DatabaseService();
        }
        return databaseService;
    }
    //zapisujemy grę do bazy
    public void putGame(Game game)
    {
        Transaction transaction = null;
        try {
            System.out.println("check");
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(game);
            transaction.commit();
        }catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    public Game getGame(int id)
    {
        Transaction transaction = null;
        Game game = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            game = session.get(Game.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
        return game;
    }

    public void refreshGame(Game game)
    {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.refresh(game);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    public void mergeGame(Game game)
    {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(game);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }
}
