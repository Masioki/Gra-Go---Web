package GoOnline.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    @Autowired
    private SessionFactory sessionFactory;


    //przyklad prostego get i tranzakcji, tranzakcja tutaj jest niepotrzebna, tylko dla przykladu
    public Game getGame(int gameID) {
        Transaction transaction = null;
        Game game = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            game = session.get(Game.class, gameID);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
        return game;
    }

    //przyklad wlasnego polecenia SQL
    public GameStatus getGameStatus(int gameID) {
        GameStatus gameStatus = null;
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT gameStatus FROM Game WHERE Game.gameID = :gameID";
            gameStatus = session.createNativeQuery(sql, GameStatus.class)
                    .setParameter("gameID", gameID)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameStatus;
    }

}
