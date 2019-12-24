package GoOnline.repositories;

import GoOnline.domain.Game.Game;
import GoOnline.domain.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;

@Repository
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    //zwroc null gdy nie znaleziono
    public Player getPlayer(String username) {
        Player player = null;
        try (Session session = sessionFactory.openSession()) {
            player = session.byNaturalId(Player.class)
                    .using("username",username)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }

    public void save(Player player){
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }
}

