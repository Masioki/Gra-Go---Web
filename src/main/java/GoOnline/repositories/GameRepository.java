package GoOnline.repositories;

import GoOnline.domain.Game.Game;
import GoOnline.domain.Player;
import GoOnline.dto.GameData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GameRepository {

    @Autowired
    private SessionFactory sessionFactory;



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

    //zwraca ID zapisanej gry, bodajze metoda session.saveOrUpdate zwraca ID
    public int save(Game g) {
        Transaction transaction = null;
        int id = -1;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            //zapisujemy otrzymane id
            id = (Integer) session.save(g);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
        return id;
    }

    //wez gry ktore maja status WAITING
    public List<GameData> getActiveGames() {
        List<GameData> gamesData = new ArrayList<GameData>();
        List<Game> games = new ArrayList<Game>();
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            //TODO
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
        return null;
    }
}
