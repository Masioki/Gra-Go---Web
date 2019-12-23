package GoOnline.domain.Game;

import GoOnline.domain.Player;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "move")//nie trzeba tego name pisac
public class Move implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @ManyToOne
    private Player player;

    @Column(name = "x")
    private int x;
    @Column(name = "y")
    private int y;

    @Column(name = "gameID", nullable = false)
    private int gameID;

    @Column(name = "color", nullable = false)
    private boolean white;

    @Column(nullable = false)
    private MoveType moveType;

    public Move(int x, int y, boolean white) {
        this.x = x;
        this.y = y;
        this.white = white;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }


}
