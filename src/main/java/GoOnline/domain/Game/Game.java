package GoOnline.domain.Game;

import GoOnline.domain.GameCommandType;
import GoOnline.domain.PawnColor;
import GoOnline.domain.Player;
import GoOnline.dto.GameData;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static GoOnline.domain.Game.GridState.*;

@Entity
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameID;

    private String ownerUsername;

    @OneToMany(mappedBy = "game")
    private List<Player> players;

    @Transient//TODO : jest potrzebne
    private List<Move> moves;

    @Column(name = "boardSize", nullable = false)
    private int boardSize;

    @Enumerated(value = EnumType.STRING)
    private GameStatus gameStatus;

    private int movesCount;

    //mówimy hibernetowi że tej zmiennej ma nie ruszać
    @Transient
    private GameLogic gameLogic;
    @Transient
    private String lastMoved;//TODO: zmienic na ostatni ruch z moves
    @Transient
    private boolean pass;

    //Dla hibernate musi byc bezargumentowy konstruktor
    public Game() {

    }

    public Game(Player owner, int boardSize) {
        players = new ArrayList<>();
        players.add(owner);
        ownerUsername = owner.getUsername();
        this.boardSize = boardSize;
        gameLogic = new GameLogic(boardSize);
        pass = false;
        gameStatus = GameStatus.WAITING;
        lastMoved = "bot";
        moves = new LinkedList<>();
    }

    //TODO uwzglednic licznik ruchu
    private void setGameLogicBoard() {
        if (gameLogic.getBoard().isEmpty()) {
            gameLogic.setGridStateMap(getBoard());
            Map<Point, GridState> result = new HashMap<>();
            List<Move> previousMoves = new ArrayList<>(moves);
            for (int x = 0; x < boardSize; x++) {
                for (int y = 0; y < boardSize; y++) {
                    result.put(new Point(x, y), EMPTY);
                }
            }
            for (Move m : previousMoves) {
                if (m.getMoveType() == MoveType.MOVE) {
                    result.replace(new Point(m.getX(), m.getY()), m.getColor());
                }
            }
        }
    }

    public Map<Point, GridState> getBoard() {
        Map<Point, GridState> result = new HashMap<>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                result.put(new Point(x, y), EMPTY);
            }
        }
        for (Move m : moves) {
            if (m.getMoveType() == MoveType.MOVE) {
                result.replace(new Point(m.getX(), m.getY()), m.getColor());
            }
        }
        return result;
    }

    public boolean addPlayer(Player player) {
        if (players.contains(player)) return true;
        if (players.size() < 2) {
            players.add(player);
            return true;
        }
        return false;
    }

    public int getOwnScore(String username) {
        if (username.equals(ownerUsername)) return gameLogic.getFinalScore(true);
        return gameLogic.getFinalScore(false);
    }

    public int getOpponentScore(String username) {
        if (username.equals(ownerUsername)) return gameLogic.getFinalScore(false);
        return gameLogic.getFinalScore(true);
    }

    public Player getOwner() {
        for (Player p : players) {
            if (p.getUsername().equals(ownerUsername)) return p;
        }
        return null;
    }
    /*
    LOGIKA
     */

    public boolean isPlayerTurn(Player player) {
        //System.out.println(player.getUsername() + " " + lastMoved);
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return !player.getUsername().equals(lastMoved);
    }

    public synchronized List<Move> move(Move move) throws Exception {
        setGameLogicBoard();
        int x = move.getX();
        int y = move.getY();
        Player player = move.getPlayer();
        List<Move> resultMoves = new ArrayList<>();
        Map<Point, GridState> previous = new HashMap<>(gameLogic.getBoard());
        if (gameStatus != GameStatus.FINISHED && isPlayerTurn(player) && gameLogic.placePawn(x, y, player.getUsername().equals(ownerUsername))) {
            GridState color;
            if (player.getUsername().equals(ownerUsername)) color = WHITE;
            else color = BLACK;
            move.setNumber(++movesCount);
            move.setColor(color);
            move.setGame(this);
            move.setMoveType(MoveType.MOVE);
            resultMoves.add(move);
            Map<Point, GridState> after = gameLogic.getBoard();
            Map<Point, GridState> changes = new HashMap<>();
            for (Point point : after.keySet()) {
                if (previous.get(point) != after.get(point)) changes.put(point, after.get(point));
            }
            for (Point p : changes.keySet()) {
                Move m = new Move();
                m.setX((int) p.getX());
                m.setY((int) p.getY());
                move.setNumber(++movesCount);
                move.setColor(EMPTY);
                move.setGame(this);
                move.setMoveType(MoveType.MOVE);
                resultMoves.add(m);
            }
            lastMoved = player.getUsername();
            pass = false;
            moves.addAll(resultMoves);
            return resultMoves;
        }
        throw new Exception("Bad move");
    }


    public synchronized Move pass(Player player) throws Exception {
        setGameLogicBoard();
        Move m = new Move();
        m.setPlayer(player);
        m.setNumber(++movesCount);
        m.setGame(this);
        if (gameStatus != GameStatus.FINISHED && isPlayerTurn(player)) {
            if (pass) {
                int white = getOwnScore(ownerUsername);
                int black = getOpponentScore(ownerUsername);
                Player p = null;
                if (white == black) {
                    m.setMoveType(MoveType.DRAW);
                    moves.add(m);
                    return m;
                } else if (white > black) {
                    for (Player pl : players) {
                        if (pl.getUsername().equals(ownerUsername)) {
                            p = pl;
                            break;
                        }
                    }
                } else {
                    for (Player pl : players) {
                        if (!pl.getUsername().equals(ownerUsername)) {
                            p = pl;
                            break;
                        }
                    }
                }
                m.setPlayer(p);
                m.setMoveType(MoveType.WIN);
                gameStatus = GameStatus.FINISHED;
                moves.add(m);
                return m;
            }
            pass = true;
            lastMoved = player.getUsername();
            m.setMoveType(MoveType.PASS);
            moves.add(m);
            return m;
        }
        throw new Exception("wrong move");
    }

    public synchronized void surrender(Player player) {
        setGameLogicBoard();
        if (gameStatus != GameStatus.FINISHED && (players.get(0).getUsername().equals(player.getUsername()) || players.get(1).getUsername().equals(player.getUsername()))) {
            Move m = new Move();
            m.setGame(this);
            m.setMoveType(MoveType.SURRENDER);
            m.setPlayer(player);
            m.setNumber(++movesCount);
            moves.add(m);
            gameStatus = GameStatus.FINISHED;
        }
    }

    /* Getters Setters */
    public GameData getGameData() {
        GameData gameData = new GameData();
        gameData.setGameID(gameID);
        gameData.setUsername(ownerUsername);
        return gameData;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String getLastMoved() {
        return lastMoved;
    }

    public void setLastMoved(String lastMoved) {
        this.lastMoved = lastMoved;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
