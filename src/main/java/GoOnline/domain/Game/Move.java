package GoOnline.domain.Game;

import GoOnline.domain.PawnColor;
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

    @Column(name = "gameID", nullable = false)//TODO powinno sie odnosic do gry a tak to tylko jakas liczba
    private Game game;

    private PawnColor color;

    @Column(nullable = false)
    private MoveType moveType;

    private int number;


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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public PawnColor getColor() {
        return color;
    }

    public void setColor(PawnColor color) {
        this.color = color;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
