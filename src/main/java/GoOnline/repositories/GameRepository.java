package GoOnline.repositories;

import GoOnline.domain.Game.Game;
import GoOnline.dto.GameData;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameRepository {

    @Autowired
    private SessionFactory sessionFactory;



    public Game getGame(int gameID) {

    }

    //zwraca ID zapisanej gry, bodajze metoda session.saveOrUpdate zwraca ID
    public int save(Game g) {
    }

    //wez gry ktore maja status WAITING
    public List<GameData> getActiveGames() {
    }
}
